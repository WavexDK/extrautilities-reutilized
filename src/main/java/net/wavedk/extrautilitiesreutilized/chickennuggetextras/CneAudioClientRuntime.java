package net.wavedk.extrautilitiesreutilized.chickennuggetextras;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.openal.AL10;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(Dist.CLIENT)
public final class CneAudioClientRuntime {
	private static final String DEFAULT_DEVICE = "System Default";
	private static final String CONFIG_FILE = "chickennuggetextras-audio.properties";
	private static final String INPUT_DEVICE_KEY = "microphoneDevice";
	private static final String INPUT_ENABLED_KEY = "microphoneEnabled";
	private static final String VOICE_RANGE_KEY = "microphoneVoiceRangeBlocks";
	private static final String PTT_KEY_KEY = "pushToTalkKey";
	private static final String MUTE_KEY_KEY = "muteToggleKey";
	private static final String MUTED_KEY = "microphoneMuted";
	private static final String VOICE_CHAT_KEY = "voiceChatEnabled";
	private static final String GAIN_KEY = "microphoneGain";
	private static final String VOICE_OUTPUT_VOLUME_KEY = "voiceOutputVolume";
	private static final double DEFAULT_VOICE_RANGE = 16.0D;
	private static final double DEFAULT_GAIN = 1.0D;
	private static final double MAX_GAIN = 5.0D;
	private static final double DEFAULT_OUTPUT_VOLUME = 1.5D;
	private static final double MAX_OUTPUT_VOLUME = 3.0D;
	private static final int MAX_CLIPS = 32;
	private static final Map<String, RecordedClip> CLIPS = new LinkedHashMap<>();
	private static final Object RECORD_LOCK = new Object();
	// Serializes all reads/writes of the audio config file. save() can be called
	// from the client thread (menu) AND the integrated-server thread (the set-volume
	// procedure in singleplayer), so two truncating writes must never interleave.
	private static final Object CONFIG_LOCK = new Object();
	private static volatile String selectedInputDevice;
	private static volatile boolean captureEnabled = true;
	private static volatile double voiceRangeBlocks = DEFAULT_VOICE_RANGE;
	// GLFW key codes; 0 means "not bound". No push-to-talk key = open mic.
	private static volatile int pushToTalkKey;
	private static volatile int muteToggleKey;
	private static volatile boolean muted;
	private static volatile boolean pushToTalkDown;
	private static boolean muteKeyWasDown;
	private static volatile boolean loaded;
	private static volatile boolean recording;
	private static volatile double liveInputLevel;
	private static volatile double microphoneGain = DEFAULT_GAIN;
	private static volatile double voiceOutputVolume = DEFAULT_OUTPUT_VOLUME;
	private static volatile boolean voiceChatEnabled = true;
	private static volatile boolean clientInWorld;
	private static volatile boolean voiceLoopStarted;
	private static volatile boolean voicePlaybackStarted;
	// Voice chat is opt-in. It activates only after a generated "Voice Chat Config"
	// element class calls applyVoiceConfig at client setup; until then nothing runs.
	private static volatile boolean voiceConfigured;
	private static volatile boolean allowMicTest = true;
	private static volatile boolean allowOutputVolume = true;
	private static volatile boolean showSoundOptions = true;
	private static volatile boolean micTestActive;
	private static volatile boolean configFileExisted;
	// Defaults supplied by the element, used the first time before the player saves.
	private static volatile double cfgVoiceRange = DEFAULT_VOICE_RANGE;
	private static volatile double cfgMicGain = DEFAULT_GAIN;
	private static volatile double cfgOutputVolume = DEFAULT_OUTPUT_VOLUME;
	private static volatile boolean cfgEnableByDefault = true;
	// Local player (listener) position, sampled each client tick so the playback thread
	// can attenuate each speaker by true distance without touching game state off-thread.
	private static volatile double listenerX;
	private static volatile double listenerY;
	private static volatile double listenerZ;
	private static volatile boolean listenerValid;
	private static final Map<UUID, VoiceStream> VOICE_STREAMS = new ConcurrentHashMap<>();

	private CneAudioClientRuntime() {
	}

	public static String inputButtonText() {
		return "Microphone: " + abbreviate(selectedInputDevice());
	}

	public static String selectedInputDevice() {
		load();
		List<String> devices = inputDevices();
		if (!devices.contains(selectedInputDevice)) {
			selectedInputDevice = DEFAULT_DEVICE;
			save();
		}
		return selectedInputDevice;
	}

	public static String cycleInputDevice() {
		load();
		List<String> devices = inputDevices();
		int index = devices.indexOf(selectedInputDevice);
		selectedInputDevice = devices.get((index + 1 + devices.size()) % devices.size());
		save();
		return selectedInputDevice;
	}

	public static List<String> inputDevices() {
		List<String> devices = new ArrayList<>();
		devices.add(DEFAULT_DEVICE);
		AudioFormat format = new AudioFormat(44100.0F, 16, 1, true, false);
		DataLine.Info targetInfo = new DataLine.Info(TargetDataLine.class, format);
		for (Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {
			try {
				Mixer mixer = AudioSystem.getMixer(mixerInfo);
				if (mixer.isLineSupported(targetInfo) || supportsAnyTargetLine(mixer)) {
					String name = mixerInfo.getName();
					if (name != null && !name.isBlank() && !devices.contains(name)) devices.add(name);
				}
			} catch (RuntimeException ignored) {
			}
		}
		return devices;
	}

	public static boolean configureInput(String deviceName, boolean enabled, boolean mono) {
		load();
		captureEnabled = enabled;
		boolean matched = true;
		String requested = deviceName == null ? "" : deviceName.trim();
		if (!requested.isEmpty()) {
			matched = false;
			for (String device : inputDevices()) {
				if (device.equalsIgnoreCase(requested) || device.toLowerCase(Locale.ROOT).contains(requested.toLowerCase(Locale.ROOT))) {
					selectedInputDevice = device;
					matched = true;
					break;
				}
			}
		}
		save();
		return matched;
	}

	public static boolean applyOutputDevice(String deviceName, boolean enabled) {
		try {
			Minecraft minecraft = Minecraft.getInstance();
			if (minecraft == null) return false;
			String requested = deviceName == null ? "" : deviceName.trim();
			minecraft.execute(() -> {
				try {
					String value = "";
					if (!requested.isEmpty() && !requested.equalsIgnoreCase(DEFAULT_DEVICE) && !requested.equalsIgnoreCase("default")) {
						for (String device : minecraft.getSoundManager().getAvailableSoundDevices()) {
							if (device.equalsIgnoreCase(requested) || device.toLowerCase(Locale.ROOT).contains(requested.toLowerCase(Locale.ROOT))) {
								value = device;
								break;
							}
						}
						if (value.isEmpty()) return;
					}
					// Setting the option triggers the vanilla sound engine reload callback.
					minecraft.options.soundDevice().set(value);
					minecraft.options.save();
				} catch (RuntimeException ignored) {
				}
			});
			return true;
		} catch (RuntimeException ignored) {
			return false;
		}
	}

	public static boolean record(String clipId, long durationMs, boolean mono) {
		load();
		if (!captureEnabled || clipId == null || clipId.isBlank()) return false;
		synchronized (RECORD_LOCK) {
			if (recording) return false;
			recording = true;
		}
		final String id = clipId.trim();
		final long duration = Math.max(50L, Math.min(600000L, durationMs));
		final AudioFormat format = new AudioFormat(44100.0F, 16, mono ? 1 : 2, true, false);
		Thread worker = new Thread(() -> {
			TargetDataLine line = null;
			try {
				line = openInputLine(format);
				if (line == null) return;
				line.open(format);
				line.start();
				ByteArrayOutputStream buffer = new ByteArrayOutputStream();
				byte[] chunk = new byte[4096];
				long deadline = System.currentTimeMillis() + duration;
				while (System.currentTimeMillis() < deadline) {
					int read = line.read(chunk, 0, chunk.length);
					if (read <= 0) break;
					if (captureGateOpen()) {
						applyGainToPcmBytes(chunk, read);
						liveInputLevel = rmsLevel(chunk, read);
					} else {
						// Push-to-talk released or microphone muted: keep the clip
						// timeline intact but write silence so no voice leaks through.
						Arrays.fill(chunk, 0, read, (byte) 0);
						liveInputLevel = 0.0D;
					}
					buffer.write(chunk, 0, read);
				}
				storeClip(id, new RecordedClip(buffer.toByteArray(), format.getSampleRate(), format.getChannels()));
			} catch (Exception ignored) {
			} finally {
				if (line != null) {
					try {
						line.stop();
						line.close();
					} catch (RuntimeException ignored) {
					}
				}
				liveInputLevel = 0.0D;
				recording = false;
			}
		}, "CNE-MicRecord");
		worker.setDaemon(true);
		worker.start();
		return true;
	}

	public static boolean play(String clipId, boolean monoOutput) {
		if (clipId == null || clipId.isBlank()) return false;
		RecordedClip clip;
		synchronized (CLIPS) {
			clip = CLIPS.get(clipId.trim());
		}
		if (clip == null || clip.data().length == 0) return false;
		final RecordedClip target = monoOutput && clip.channels() == 2 ? downmixToMono(clip) : clip;
		Thread worker = new Thread(() -> {
			SourceDataLine line = null;
			try {
				AudioFormat format = new AudioFormat(target.sampleRate(), 16, target.channels(), true, false);
				line = AudioSystem.getSourceDataLine(format);
				line.open(format);
				line.start();
				byte[] data = target.data();
				int offset = 0;
				while (offset < data.length) {
					int written = line.write(data, offset, Math.min(8192, data.length - offset));
					if (written <= 0) break;
					offset += written;
				}
				line.drain();
			} catch (Exception ignored) {
			} finally {
				if (line != null) {
					try {
						line.stop();
						line.close();
					} catch (RuntimeException ignored) {
					}
				}
			}
		}, "CNE-MicPlayback");
		worker.setDaemon(true);
		worker.start();
		return true;
	}

	public static double inputLevel() {
		return (recording || micTestActive) ? Math.max(0.0D, Math.min(1.0D, liveInputLevel)) : 0.0D;
	}

	public static double voiceRange() {
		load();
		return voiceRangeBlocks;
	}

	public static void configureVoiceRange(double blocks) {
		load();
		voiceRangeBlocks = Double.isFinite(blocks) ? Math.max(1.0D, Math.min(256.0D, blocks)) : DEFAULT_VOICE_RANGE;
		save();
	}

	public static boolean captureGateOpen() {
		load();
		return !muted && (pushToTalkKey <= 0 || pushToTalkDown);
	}

	public static double microphoneGain() {
		load();
		return microphoneGain;
	}

	public static void configureMicrophoneGain(double gain) {
		load();
		microphoneGain = Double.isFinite(gain) ? Math.max(0.0D, Math.min(MAX_GAIN, gain)) : DEFAULT_GAIN;
		save();
	}

	// Microphone volume: amplifies every captured PCM sample, then SOFT-clips it. Quiet
	// and mid-level speech is boosted linearly (so a quiet mic gets reliably louder),
	// while peaks are rounded off with a smooth knee instead of being hard-clamped - hard
	// clamping is what turned a high gain into harsh, scratchy distortion. Applied to both
	// the voice-chat stream and clip recording, BEFORE voice-activation detection.
	private static void applyGainToShorts(short[] samples) {
		double gain = microphoneGain;
		if (gain == 1.0D || samples.length == 0) return;
		for (int i = 0; i < samples.length; i++) {
			samples[i] = softClip((samples[i] / 32768.0D) * gain);
		}
	}

	private static void applyGainToPcmBytes(byte[] data, int length) {
		double gain = microphoneGain;
		if (gain == 1.0D) return;
		int samples = length / 2;
		for (int i = 0; i < samples; i++) {
			int sample = (short) ((data[i * 2 + 1] << 8) | (data[i * 2] & 0xFF));
			short scaled = softClip((sample / 32768.0D) * gain);
			data[i * 2] = (byte) (scaled & 0xFF);
			data[i * 2 + 1] = (byte) ((scaled >> 8) & 0xFF);
		}
	}

	// Smooth saturating limiter. |x| up to KNEE passes through linearly (full boost for
	// normal speech); louder peaks are compressed with a tanh curve that eases toward
	// full scale instead of clipping, so boosting the mic never turns harsh. x is the
	// sample already scaled by gain and normalised to roughly [-1, 1].
	private static short softClip(double x) {
		double knee = 0.7D;
		double magnitude = Math.abs(x);
		double shaped;
		if (magnitude <= knee) {
			shaped = x;
		} else {
			double over = (magnitude - knee) / (1.0D - knee);
			shaped = Math.signum(x) * (knee + (1.0D - knee) * Math.tanh(over));
		}
		int value = (int) Math.round(shaped * 32767.0D);
		return (short) Math.max(Short.MIN_VALUE, Math.min(Short.MAX_VALUE, value));
	}

	public static boolean isVoiceChatEnabled() {
		load();
		return voiceChatEnabled;
	}

	public static void setVoiceChatEnabled(boolean value) {
		load();
		voiceChatEnabled = value;
		save();
	}

	// ---- Voice Chat Config element gate ----
	//
	// applyVoiceConfig is generated and called (once, at client setup) by the class a
	// "Voice Chat Config" mod element produces. Until it runs, voiceConfigured is false
	// and every voice path stays inert: no sound-menu rows, no microphone capture, no
	// playback. That makes proximity voice chat fully opt-in per mod, and lets the mod
	// maker set the default range, microphone volume and voice output volume.
	public static void applyVoiceConfig(boolean enableByDefault, double range, double gain, double outputVolume, boolean micTest, boolean outputVolumeControl, boolean showSoundMenu) {
		cfgEnableByDefault = enableByDefault;
		cfgVoiceRange = Double.isFinite(range) ? Math.max(1.0D, Math.min(256.0D, range)) : DEFAULT_VOICE_RANGE;
		cfgMicGain = Double.isFinite(gain) ? Math.max(0.0D, Math.min(MAX_GAIN, gain)) : DEFAULT_GAIN;
		cfgOutputVolume = Double.isFinite(outputVolume) ? Math.max(0.0D, Math.min(MAX_OUTPUT_VOLUME, outputVolume)) : DEFAULT_OUTPUT_VOLUME;
		allowMicTest = micTest;
		allowOutputVolume = outputVolumeControl;
		showSoundOptions = showSoundMenu;
		voiceConfigured = true;
		load();
		// First run (no saved audio config yet): seed the live values from the element
		// defaults, whether load() happened before or after this call.
		synchronized (CONFIG_LOCK) {
			if (!configFileExisted) {
				voiceRangeBlocks = cfgVoiceRange;
				microphoneGain = cfgMicGain;
				voiceOutputVolume = cfgOutputVolume;
				voiceChatEnabled = cfgEnableByDefault;
			}
		}
	}

	public static boolean isVoiceConfigured() {
		return voiceConfigured;
	}

	public static double voiceOutputVolume() {
		load();
		return voiceOutputVolume;
	}

	public static void configureVoiceOutputVolume(double volume) {
		load();
		voiceOutputVolume = Double.isFinite(volume) ? Math.max(0.0D, Math.min(MAX_OUTPUT_VOLUME, volume)) : DEFAULT_OUTPUT_VOLUME;
		save();
	}

	// ---- Microphone self-test (loopback) ----
	//
	// Pipes the microphone straight back to the speakers/headphones so the player can hear
	// and tune their own voice. The voice capture loop pauses while this runs so the two
	// do not fight over the same input device.
	public static boolean isMicTestActive() {
		return micTestActive;
	}

	public static void setMicTest(boolean on) {
		load();
		if (!on) {
			micTestActive = false;
			return;
		}
		if (micTestActive) return;
		micTestActive = true;
		Thread worker = new Thread(CneAudioClientRuntime::runMicTest, "CNE-MicTest");
		worker.setDaemon(true);
		worker.start();
	}

	private static void runMicTest() {
		TargetDataLine in = null;
		SourceDataLine out = null;
		try {
			Object[] opened = null;
			for (int attempt = 0; attempt < 12 && micTestActive; attempt++) {
				opened = openVoiceLine();
				if (opened != null) break;
				Thread.sleep(100L);
			}
			if (opened == null) {
				micTestActive = false;
				return;
			}
			in = (TargetDataLine) opened[0];
			int rate = (Integer) opened[1];
			AudioFormat format = new AudioFormat(rate, 16, 1, true, false);
			out = AudioSystem.getSourceDataLine(format);
			out.open(format);
			out.start();
			byte[] chunk = new byte[Math.max(640, (rate / 25) * 2)];
			long deadline = System.currentTimeMillis() + 30000L;
			while (micTestActive && System.currentTimeMillis() < deadline) {
				int read = in.read(chunk, 0, chunk.length);
				if (read <= 0) break;
				applyGainToPcmBytes(chunk, read);
				liveInputLevel = rmsLevel(chunk, read);
				int offset = 0;
				while (offset < read) {
					int written = out.write(chunk, offset, read - offset);
					if (written <= 0) break;
					offset += written;
				}
			}
		} catch (Exception ignored) {
		} finally {
			liveInputLevel = 0.0D;
			micTestActive = false;
			if (in != null) {
				try {
					in.stop();
					in.close();
				} catch (RuntimeException ignored) {
				}
			}
			if (out != null) {
				try {
					out.drain();
					out.stop();
					out.close();
				} catch (RuntimeException ignored) {
				}
			}
		}
	}

	// ---- Proximity voice chat ----
	//
	// Capture side: while in a world (and voice chat + capture enabled, mic not
	// muted, push-to-talk satisfied, no clip recording running), the microphone is
	// read continuously, resampled to 16 kHz mono, gated by a small voice-activation
	// threshold, encoded as G.711 mu-law and sent to the server in 100 ms frames.

	private static void ensureVoiceCaptureLoop() {
		if (voiceLoopStarted) return;
		synchronized (RECORD_LOCK) {
			if (voiceLoopStarted) return;
			voiceLoopStarted = true;
		}
		Thread worker = new Thread(() -> {
			TargetDataLine line = null;
			int lineRate = 16000;
			int hangover = 0;
			while (true) {
				try {
					load();
					boolean shouldCapture = voiceConfigured && clientInWorld && voiceChatEnabled && captureEnabled && !recording && !micTestActive;
					if (!shouldCapture) {
						if (line != null) {
							try {
								line.stop();
								line.close();
							} catch (RuntimeException ignored) {
							}
							line = null;
						}
						Thread.sleep(250L);
						continue;
					}
					if (line == null) {
						Object[] opened = openVoiceLine();
						if (opened == null) {
							Thread.sleep(1000L);
							continue;
						}
						line = (TargetDataLine) opened[0];
						lineRate = (Integer) opened[1];
					}
					int chunkBytes = Math.max(640, (lineRate / 10) * 2);
					byte[] chunk = new byte[chunkBytes];
					int read = 0;
					while (read < chunkBytes) {
						int r = line.read(chunk, read, chunkBytes - read);
						if (r <= 0) break;
						read += r;
					}
					if (read <= 0) {
						Thread.sleep(50L);
						continue;
					}
					short[] pcm16k = resampleTo16k(bytesToShorts(chunk, read), lineRate);
					applyGainToShorts(pcm16k);
					double rms = rmsOfShorts(pcm16k);
					boolean loud = rms > 0.012D;
					if (loud) hangover = 4;
					else if (hangover > 0) hangover--;
					if (!captureGateOpen() || (!loud && hangover <= 0)) continue;
					byte[] encoded = encodeMulaw(pcm16k);
					float range = (float) voiceRange();
					Minecraft minecraft = Minecraft.getInstance();
					if (minecraft != null && minecraft.getConnection() != null) {
						minecraft.execute(() -> {
							try {
								PacketDistributor.sendToServer(new CneVoiceRuntime.VoiceUpMessage(range, encoded));
							} catch (RuntimeException ignored) {
							}
						});
					}
				} catch (InterruptedException e) {
					return;
				} catch (Throwable ignored) {
					try {
						Thread.sleep(500L);
					} catch (InterruptedException e) {
						return;
					}
				}
			}
		}, "CNE-VoiceChatCapture");
		worker.setDaemon(true);
		worker.start();
	}

	private static Object[] openVoiceLine() {
		for (int rate : new int[]{16000, 48000, 44100, 32000, 22050}) {
			try {
				AudioFormat format = new AudioFormat(rate, 16, 1, true, false);
				TargetDataLine line = openInputLine(format);
				if (line == null) continue;
				line.open(format);
				line.start();
				return new Object[]{line, rate};
			} catch (Exception ignored) {
			}
		}
		return null;
	}

	// Playback side: each speaking player gets a streaming positional mono OpenAL
	// source inside Minecraft's own context - directional, fading to silent at the
	// speaker's voice range, and scaled by the separate voice chat volume slider
	// (independent of the master and player sound sliders).

	public static void enqueueIncomingVoice(UUID speaker, double x, double y, double z, float range, byte[] mulaw) {
		if (!voiceConfigured || speaker == null || mulaw == null || mulaw.length == 0) return;
		VoiceStream stream = VOICE_STREAMS.computeIfAbsent(speaker, key -> new VoiceStream());
		stream.x = x;
		stream.y = y;
		stream.z = z;
		stream.range = Math.max(1.0F, Math.min(256.0F, range));
		stream.lastData = System.currentTimeMillis();
		short[] pcm = new short[mulaw.length];
		for (int i = 0; i < mulaw.length; i++) {
			pcm[i] = mulawToLinear(mulaw[i]);
		}
		synchronized (stream.pending) {
			stream.pending.add(pcm);
			while (stream.pending.size() > 20) {
				stream.pending.poll();
			}
		}
		ensureVoicePlaybackLoop();
	}

	private static void ensureVoicePlaybackLoop() {
		if (voicePlaybackStarted) return;
		synchronized (RECORD_LOCK) {
			if (voicePlaybackStarted) return;
			voicePlaybackStarted = true;
		}
		Thread worker = new Thread(() -> {
			while (true) {
				try {
					Thread.sleep(20L);
					if (VOICE_STREAMS.isEmpty()) continue;
					double outVol = voiceOutputVolume();
					boolean haveListener = listenerValid;
					double lx = listenerX;
					double ly = listenerY;
					double lz = listenerZ;
					long now = System.currentTimeMillis();
					Iterator<Map.Entry<UUID, VoiceStream>> iterator = VOICE_STREAMS.entrySet().iterator();
					while (iterator.hasNext()) {
						VoiceStream stream = iterator.next().getValue();
						try {
							if (stream.source == 0) {
								AL10.alGetError();
								stream.source = AL10.alGenSources();
								if (AL10.alGetError() != AL10.AL_NO_ERROR || stream.source == 0) {
									iterator.remove();
									continue;
								}
								AL10.alSourcef(stream.source, AL10.AL_REFERENCE_DISTANCE, 1.0F);
								// We attenuate by distance ourselves (below), so OpenAL's own
								// distance rolloff is turned off - the positional source is used
								// only for stereo direction, never for how loud the voice is.
								AL10.alSourcef(stream.source, AL10.AL_ROLLOFF_FACTOR, 0.0F);
							}
							int processed = AL10.alGetSourcei(stream.source, AL10.AL_BUFFERS_PROCESSED);
							while (processed-- > 0) {
								int buffer = AL10.alSourceUnqueueBuffers(stream.source);
								AL10.alDeleteBuffers(buffer);
							}
							synchronized (stream.pending) {
								short[] pcm;
								while ((pcm = stream.pending.poll()) != null) {
									int buffer = AL10.alGenBuffers();
									java.nio.ByteBuffer data = BufferUtils.createByteBuffer(pcm.length * 2);
									for (short sample : pcm) {
										data.put((byte) (sample & 0xFF));
										data.put((byte) ((sample >> 8) & 0xFF));
									}
									data.flip();
									AL10.alBufferData(buffer, AL10.AL_FORMAT_MONO16, data, 16000);
									AL10.alSourceQueueBuffers(stream.source, buffer);
								}
							}
							AL10.alSource3f(stream.source, AL10.AL_POSITION, (float) stream.x, (float) stream.y, (float) stream.z);
							// Fade the voice out linearly with real distance to the speaker,
							// reaching silence at their voice range. This is done here (not via
							// OpenAL's distance model) so it is reliable regardless of how the
							// game has configured the model, then scaled by the separate voice
							// chat volume - independent of the Master and Players sliders.
							float attenuation = 1.0F;
							if (haveListener) {
								double dx = stream.x - lx;
								double dy = stream.y - ly;
								double dz = stream.z - lz;
								double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
								double range = Math.max(1.0D, stream.range);
								attenuation = (float) Math.max(0.0D, Math.min(1.0D, 1.0D - distance / range));
							}
							float gain = (float) Math.max(0.0D, Math.min(MAX_OUTPUT_VOLUME, attenuation * outVol));
							AL10.alSourcef(stream.source, AL10.AL_GAIN, gain);
							int queued = AL10.alGetSourcei(stream.source, AL10.AL_BUFFERS_QUEUED);
							if (queued > 0 && AL10.alGetSourcei(stream.source, AL10.AL_SOURCE_STATE) != AL10.AL_PLAYING) {
								AL10.alSourcePlay(stream.source);
							}
							if (queued == 0 && now - stream.lastData > 3000L) {
								AL10.alDeleteSources(stream.source);
								iterator.remove();
							}
						} catch (Throwable streamError) {
							try {
								if (stream.source != 0) AL10.alDeleteSources(stream.source);
							} catch (Throwable ignored) {
							}
							iterator.remove();
						}
					}
				} catch (InterruptedException e) {
					return;
				} catch (Throwable ignored) {
				}
			}
		}, "CNE-VoiceChatPlayback");
		worker.setDaemon(true);
		worker.start();
	}

	private static short[] bytesToShorts(byte[] data, int length) {
		int samples = length / 2;
		short[] out = new short[samples];
		for (int i = 0; i < samples; i++) {
			out[i] = (short) ((data[i * 2 + 1] << 8) | (data[i * 2] & 0xFF));
		}
		return out;
	}

	private static short[] resampleTo16k(short[] input, int sourceRate) {
		if (sourceRate == 16000 || input.length == 0) return input;
		int outLength = Math.max(1, (int) ((long) input.length * 16000L / sourceRate));
		short[] out = new short[outLength];
		double step = input.length / (double) outLength;
		for (int i = 0; i < outLength; i++) {
			double pos = i * step;
			int index = (int) pos;
			int next = Math.min(input.length - 1, index + 1);
			double frac = pos - index;
			out[i] = (short) (input[index] * (1.0D - frac) + input[next] * frac);
		}
		return out;
	}

	private static double rmsOfShorts(short[] samples) {
		if (samples.length == 0) return 0.0D;
		double sum = 0.0D;
		for (short sample : samples) {
			double normalized = sample / 32768.0D;
			sum += normalized * normalized;
		}
		return Math.sqrt(sum / samples.length);
	}

	private static byte[] encodeMulaw(short[] samples) {
		byte[] out = new byte[samples.length];
		for (int i = 0; i < samples.length; i++) {
			out[i] = linearToMulaw(samples[i]);
		}
		return out;
	}

	private static byte linearToMulaw(int pcm) {
		int sign = (pcm >> 8) & 0x80;
		if (sign != 0) pcm = -pcm;
		if (pcm > 32635) pcm = 32635;
		pcm += 0x84;
		int exponent = 7;
		for (int mask = 0x4000; (pcm & mask) == 0 && exponent > 0; exponent--, mask >>= 1) {
		}
		int mantissa = (pcm >> (exponent + 3)) & 0x0F;
		return (byte) ~(sign | (exponent << 4) | mantissa);
	}

	private static short mulawToLinear(byte mu) {
		int value = ~mu & 0xFF;
		int sign = value & 0x80;
		int exponent = (value >> 4) & 0x07;
		int mantissa = value & 0x0F;
		int sample = ((mantissa << 3) + 0x84) << exponent;
		sample -= 0x84;
		return (short) (sign != 0 ? -sample : sample);
	}

	private static final class VoiceStream {
		int source;
		final ArrayDeque<short[]> pending = new ArrayDeque<>();
		volatile double x;
		volatile double y;
		volatile double z;
		volatile float range = 16.0F;
		volatile long lastData;
	}

	public static boolean isMuted() {
		load();
		return muted;
	}

	public static void setMuted(boolean value) {
		load();
		muted = value;
		save();
	}

	@SubscribeEvent
	public static void onClientTick(ClientTickEvent.Post event) {
		Minecraft minecraft = Minecraft.getInstance();
		if (minecraft == null || minecraft.getWindow() == null) return;
		load();
		clientInWorld = minecraft.level != null && minecraft.player != null;
		if (minecraft.player != null) {
			listenerX = minecraft.player.getX();
			listenerY = minecraft.player.getEyeY();
			listenerZ = minecraft.player.getZ();
			listenerValid = true;
		} else {
			listenerValid = false;
		}
		// Stop the microphone self-test once the player leaves the sound options screen.
		if (micTestActive && minecraft.screen == null) {
			micTestActive = false;
		}
		if (voiceConfigured) {
			ensureVoiceCaptureLoop();
		}
		long window = minecraft.getWindow().getWindow();
		pushToTalkDown = pushToTalkKey > 0 && GLFW.glfwGetKey(window, pushToTalkKey) == GLFW.GLFW_PRESS;
		// Only react to the mute key while no screen is open, so typing in chat or
		// binding keys in the options menu never toggles the microphone.
		boolean muteDown = minecraft.screen == null && muteToggleKey > 0 && GLFW.glfwGetKey(window, muteToggleKey) == GLFW.GLFW_PRESS;
		if (muteDown && !muteKeyWasDown) {
			muted = !muted;
			save();
			if (minecraft.player != null) {
				minecraft.player.displayClientMessage(Component.literal(muted ? "Microphone muted" : "Microphone unmuted"), true);
			}
		}
		muteKeyWasDown = muteDown;
	}

	/**
	 * Builds the extra microphone rows for the vanilla Music & Sounds screen.
	 * Called from the SoundOptionsScreen mixin; keeping the construction here means
	 * menu changes only need a plugin template update, not a new mixin.
	 */
	public static List<AbstractWidget> buildSoundOptionsWidgets() {
		load();
		List<AbstractWidget> widgets = new ArrayList<>();
		// Opt-in: with no "Voice Chat Config" mod element, applyVoiceConfig is never called,
		// so we add no microphone or voice rows to the sound menu at all. The element can also
		// hide every row (showSoundOptions=false) while voice chat still runs on its defaults.
		if (!voiceConfigured || !showSoundOptions) return widgets;
		Button device = Button.builder(Component.literal(inputButtonText()), button -> {
			cycleInputDevice();
			button.setMessage(Component.literal(inputButtonText()));
		}).build();
		device.setTooltip(Tooltip.create(Component.literal("Cycles which microphone device is used for recording.")));
		widgets.add(device);
		GainSliderButton gainSlider = new GainSliderButton();
		gainSlider.setTooltip(Tooltip.create(Component.literal("Microphone volume. Boosts your captured voice for recordings and proximity voice chat (0% mutes, 100% is unchanged, up to 500%). Loud input is clamped so it never distorts into noise.")));
		widgets.add(gainSlider);
		KeyCaptureButton ptt = new KeyCaptureButton("Push to Talk", "OFF (open mic)", () -> pushToTalkKey, code -> {
			pushToTalkKey = code;
			save();
		});
		ptt.setTooltip(Tooltip.create(Component.literal("Click, then press a key to bind push-to-talk. While bound, the microphone only captures voice while the key is held. Backspace clears the bind (open mic).")));
		widgets.add(ptt);
		KeyCaptureButton muteBind = new KeyCaptureButton("Mute Key", "none", () -> muteToggleKey, code -> {
			muteToggleKey = code;
			save();
		});
		muteBind.setTooltip(Tooltip.create(Component.literal("Optional: click, then press a key to bind a microphone mute toggle. Backspace clears the bind.")));
		widgets.add(muteBind);
		Button mute = Button.builder(Component.literal(muteButtonText()), button -> {
			setMuted(!isMuted());
			button.setMessage(Component.literal(muteButtonText()));
		}).build();
		mute.setTooltip(Tooltip.create(Component.literal("Instantly mutes or unmutes the microphone.")));
		widgets.add(mute);
		Button voiceChat = Button.builder(Component.literal(voiceChatButtonText()), button -> {
			setVoiceChatEnabled(!isVoiceChatEnabled());
			button.setMessage(Component.literal(voiceChatButtonText()));
		}).build();
		voiceChat.setTooltip(Tooltip.create(Component.literal("Proximity voice chat: streams your microphone to nearby players (push-to-talk, mute and voice range all apply). Voices come from the speaker's direction and fade with distance.")));
		widgets.add(voiceChat);
		if (allowOutputVolume) {
			VoiceVolumeSliderButton voiceVolume = new VoiceVolumeSliderButton();
			voiceVolume.setTooltip(Tooltip.create(Component.literal("Voice chat volume: how loud other players' voices are. Separate from the Master and Players sliders, so you can boost voices without changing your game volume (0% mutes voices, up to " + (int) Math.round(MAX_OUTPUT_VOLUME * 100.0D) + "%).")));
			widgets.add(voiceVolume);
		}
		if (allowMicTest) {
			Button micTest = Button.builder(Component.literal(micTestButtonText()), button -> {
				setMicTest(!isMicTestActive());
				button.setMessage(Component.literal(micTestButtonText()));
			}).build();
			micTest.setTooltip(Tooltip.create(Component.literal("Mic test: loops your microphone back to your own speakers/headphones so you can hear yourself and tune the microphone volume. Click again to stop; it also stops when you leave this screen.")));
			widgets.add(micTest);
		}
		return widgets;
	}

	private static String voiceChatButtonText() {
		return isVoiceChatEnabled() ? "Voice Chat: On" : "Voice Chat: Off";
	}

	/** Microphone volume slider (0% to 500%, default 100%), matching the vanilla sound sliders. */
	public static final class GainSliderButton extends AbstractSliderButton {
		GainSliderButton() {
			super(0, 0, 150, 20, Component.empty(), microphoneGain() / MAX_GAIN);
			updateMessage();
		}

		@Override
		protected void updateMessage() {
			setMessage(Component.literal("Microphone Volume: " + Math.round(microphoneGain() * 100.0D) + "%"));
		}

		@Override
		protected void applyValue() {
			// Save on every value change (mouse drag AND keyboard arrow keys, which
			// never fire onRelease) so a keyboard-only adjustment is not lost on
			// restart. The config file is tiny and save() is serialized, so the
			// extra writes during a drag are negligible - matching how every other
			// control in the sound menu persists immediately in its callback.
			configureMicrophoneGain(this.value * MAX_GAIN);
		}
	}

	/** Voice chat output volume slider (0% to MAX_OUTPUT_VOLUME). Independent of the master/players sliders. */
	public static final class VoiceVolumeSliderButton extends AbstractSliderButton {
		VoiceVolumeSliderButton() {
			super(0, 0, 150, 20, Component.empty(), voiceOutputVolume() / MAX_OUTPUT_VOLUME);
			updateMessage();
		}

		@Override
		protected void updateMessage() {
			setMessage(Component.literal("Voice Chat Volume: " + Math.round(voiceOutputVolume() * 100.0D) + "%"));
		}

		@Override
		protected void applyValue() {
			configureVoiceOutputVolume(this.value * MAX_OUTPUT_VOLUME);
		}
	}

	private static String micTestButtonText() {
		return isMicTestActive() ? "Mic Test: ON (hearing yourself)" : "Test Microphone";
	}

	private static String muteButtonText() {
		return isMuted() ? "Microphone: Muted" : "Microphone: On";
	}

	private static String keyDisplayName(int keyCode) {
		if (keyCode <= 0) return "";
		try {
			return InputConstants.Type.KEYSYM.getOrCreate(keyCode).getDisplayName().getString();
		} catch (RuntimeException e) {
			return "Key " + keyCode;
		}
	}

	/**
	 * Button that arms on click and binds the next pressed key. Backspace or
	 * Delete clears the binding; losing focus disarms without changing it.
	 */
	public static final class KeyCaptureButton extends Button {
		private final String label;
		private final String unboundText;
		private final java.util.function.IntSupplier getter;
		private final java.util.function.IntConsumer setter;
		private boolean armed;

		KeyCaptureButton(String label, String unboundText, java.util.function.IntSupplier getter, java.util.function.IntConsumer setter) {
			super(0, 0, 150, 20, Component.empty(), button -> {
			}, DEFAULT_NARRATION);
			this.label = label;
			this.unboundText = unboundText;
			this.getter = getter;
			this.setter = setter;
			refreshText();
		}

		@Override
		public void onPress() {
			armed = !armed;
			refreshText();
		}

		@Override
		public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
			if (armed) {
				if (keyCode == GLFW.GLFW_KEY_BACKSPACE || keyCode == GLFW.GLFW_KEY_DELETE) {
					setter.accept(0);
				} else if (keyCode != GLFW.GLFW_KEY_ESCAPE) {
					setter.accept(keyCode);
				}
				armed = false;
				refreshText();
				return true;
			}
			return super.keyPressed(keyCode, scanCode, modifiers);
		}

		@Override
		public void setFocused(boolean focused) {
			super.setFocused(focused);
			if (!focused && armed) {
				armed = false;
				refreshText();
			}
		}

		private void refreshText() {
			if (armed) {
				setMessage(Component.literal(label + ": press a key..."));
			} else {
				int code = getter.getAsInt();
				setMessage(Component.literal(label + ": " + (code <= 0 ? unboundText : keyDisplayName(code))));
			}
		}
	}

	/**
	 * Plays a recorded clip as a real 3D sound source inside Minecraft's own OpenAL
	 * context. The clip is forced to mono first: OpenAL only spatializes mono
	 * sources, which is exactly what makes the voice directional for the listener.
	 * With the game's linear-clamped distance model the source fades to silent at
	 * the given range (in blocks). The source follows the speaker entity while it
	 * plays. rangeBlocks <= 0 uses the configured microphone voice range.
	 */
	public static boolean playPositional(String clipId, Entity speaker, double rangeBlocks) {
		if (clipId == null || clipId.isBlank() || speaker == null) return false;
		RecordedClip clip;
		synchronized (CLIPS) {
			clip = CLIPS.get(clipId.trim());
		}
		if (clip == null || clip.data().length == 0) return false;
		final RecordedClip monoClip = clip.channels() == 2 ? downmixToMono(clip) : clip;
		final double range = rangeBlocks > 0 ? Math.max(1.0D, Math.min(256.0D, rangeBlocks)) : voiceRange();
		Thread worker = new Thread(() -> {
			int source = 0;
			int buffer = 0;
			try {
				AL10.alGetError();
				source = AL10.alGenSources();
				buffer = AL10.alGenBuffers();
				if (AL10.alGetError() != AL10.AL_NO_ERROR || source == 0 || buffer == 0) return;
				byte[] data = monoClip.data();
				java.nio.ByteBuffer pcm = BufferUtils.createByteBuffer(data.length);
				pcm.put(data).flip();
				AL10.alBufferData(buffer, AL10.AL_FORMAT_MONO16, pcm, (int) monoClip.sampleRate());
				AL10.alSourcei(source, AL10.AL_BUFFER, buffer);
				// Mirror how the vanilla sound engine sets up attenuation so the
				// voice obeys the same linear-clamped distance model as game sounds.
				AL10.alSourcef(source, AL10.AL_REFERENCE_DISTANCE, 0.0F);
				AL10.alSourcef(source, AL10.AL_ROLLOFF_FACTOR, 1.0F);
				AL10.alSourcef(source, AL10.AL_MAX_DISTANCE, (float) range);
				AL10.alSource3f(source, AL10.AL_POSITION, (float) speaker.getX(), (float) speaker.getEyeY(), (float) speaker.getZ());
				AL10.alSourcePlay(source);
				while (AL10.alGetSourcei(source, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING) {
					AL10.alSource3f(source, AL10.AL_POSITION, (float) speaker.getX(), (float) speaker.getEyeY(), (float) speaker.getZ());
					Thread.sleep(50L);
				}
			} catch (Throwable ignored) {
			} finally {
				try {
					if (source != 0) AL10.alDeleteSources(source);
					if (buffer != 0) AL10.alDeleteBuffers(buffer);
				} catch (Throwable ignored) {
				}
			}
		}, "CNE-MicPositional");
		worker.setDaemon(true);
		worker.start();
		return true;
	}

	private static TargetDataLine openInputLine(AudioFormat format) {
		load();
		DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
		String wanted = selectedInputDevice;
		if (wanted != null && !wanted.isBlank() && !DEFAULT_DEVICE.equals(wanted)) {
			for (Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {
				if (!wanted.equals(mixerInfo.getName())) continue;
				try {
					Mixer mixer = AudioSystem.getMixer(mixerInfo);
					if (mixer.isLineSupported(info)) {
						return (TargetDataLine) mixer.getLine(info);
					}
				} catch (Exception ignored) {
				}
			}
		}
		try {
			return (TargetDataLine) AudioSystem.getLine(info);
		} catch (Exception ignored) {
			return null;
		}
	}

	private static void storeClip(String id, RecordedClip clip) {
		synchronized (CLIPS) {
			CLIPS.remove(id);
			while (CLIPS.size() >= MAX_CLIPS) {
				Iterable<String> keys = new ArrayList<>(CLIPS.keySet());
				for (String key : keys) {
					CLIPS.remove(key);
					break;
				}
			}
			CLIPS.put(id, clip);
		}
	}

	private static RecordedClip downmixToMono(RecordedClip stereo) {
		byte[] source = stereo.data();
		int frames = source.length / 4;
		byte[] mono = new byte[frames * 2];
		for (int frame = 0; frame < frames; frame++) {
			int left = (short) ((source[frame * 4 + 1] << 8) | (source[frame * 4] & 0xFF));
			int right = (short) ((source[frame * 4 + 3] << 8) | (source[frame * 4 + 2] & 0xFF));
			int mixed = (left + right) / 2;
			mono[frame * 2] = (byte) (mixed & 0xFF);
			mono[frame * 2 + 1] = (byte) ((mixed >> 8) & 0xFF);
		}
		return new RecordedClip(mono, stereo.sampleRate(), 1);
	}

	private static double rmsLevel(byte[] chunk, int length) {
		int samples = length / 2;
		if (samples <= 0) return 0.0D;
		double sum = 0.0D;
		for (int i = 0; i < samples; i++) {
			int sample = (short) ((chunk[i * 2 + 1] << 8) | (chunk[i * 2] & 0xFF));
			double normalized = sample / 32768.0D;
			sum += normalized * normalized;
		}
		return Math.sqrt(sum / samples);
	}

	private static boolean supportsAnyTargetLine(Mixer mixer) {
		for (Line.Info info : mixer.getTargetLineInfo()) {
			if (TargetDataLine.class.isAssignableFrom(info.getLineClass())) return true;
		}
		return false;
	}

	private static void load() {
		if (loaded) return;
		synchronized (CONFIG_LOCK) {
			if (loaded) return;
			loadLocked();
			loaded = true;
		}
	}

	private static void loadLocked() {
		selectedInputDevice = DEFAULT_DEVICE;
		// Until the player saves their own preferences, the voice defaults come from the
		// Voice Chat Config element (cfg*), which fall back to the built-in constants.
		voiceRangeBlocks = cfgVoiceRange;
		microphoneGain = cfgMicGain;
		voiceOutputVolume = cfgOutputVolume;
		voiceChatEnabled = cfgEnableByDefault;
		File file = configFile();
		configFileExisted = file.isFile();
		if (!configFileExisted) return;
		Properties properties = new Properties();
		try (FileInputStream stream = new FileInputStream(file)) {
			properties.load(stream);
			String stored = properties.getProperty(INPUT_DEVICE_KEY);
			if (stored != null && !stored.isBlank()) selectedInputDevice = stored;
			captureEnabled = !"false".equalsIgnoreCase(properties.getProperty(INPUT_ENABLED_KEY, "true"));
			try {
				double storedRange = Double.parseDouble(properties.getProperty(VOICE_RANGE_KEY, String.valueOf(cfgVoiceRange)));
				voiceRangeBlocks = Double.isFinite(storedRange) ? Math.max(1.0D, Math.min(256.0D, storedRange)) : cfgVoiceRange;
			} catch (NumberFormatException ignored) {
			}
			try {
				pushToTalkKey = Math.max(0, Integer.parseInt(properties.getProperty(PTT_KEY_KEY, "0")));
				muteToggleKey = Math.max(0, Integer.parseInt(properties.getProperty(MUTE_KEY_KEY, "0")));
			} catch (NumberFormatException ignored) {
			}
			muted = "true".equalsIgnoreCase(properties.getProperty(MUTED_KEY, "false"));
			voiceChatEnabled = !"false".equalsIgnoreCase(properties.getProperty(VOICE_CHAT_KEY, String.valueOf(cfgEnableByDefault)));
			try {
				double storedGain = Double.parseDouble(properties.getProperty(GAIN_KEY, String.valueOf(cfgMicGain)));
				microphoneGain = Double.isFinite(storedGain) ? Math.max(0.0D, Math.min(MAX_GAIN, storedGain)) : cfgMicGain;
			} catch (NumberFormatException ignored) {
			}
			try {
				double storedOutput = Double.parseDouble(properties.getProperty(VOICE_OUTPUT_VOLUME_KEY, String.valueOf(cfgOutputVolume)));
				voiceOutputVolume = Double.isFinite(storedOutput) ? Math.max(0.0D, Math.min(MAX_OUTPUT_VOLUME, storedOutput)) : cfgOutputVolume;
			} catch (NumberFormatException ignored) {
			}
		} catch (IOException ignored) {
		}
	}

	private static void save() {
		synchronized (CONFIG_LOCK) {
			File file = configFile();
			File parent = file.getParentFile();
			if (parent != null) parent.mkdirs();
			Properties properties = new Properties();
			properties.setProperty(INPUT_DEVICE_KEY, selectedInputDevice == null ? DEFAULT_DEVICE : selectedInputDevice);
			properties.setProperty(INPUT_ENABLED_KEY, String.valueOf(captureEnabled));
			properties.setProperty(VOICE_RANGE_KEY, String.valueOf(voiceRangeBlocks));
			properties.setProperty(PTT_KEY_KEY, String.valueOf(pushToTalkKey));
			properties.setProperty(MUTE_KEY_KEY, String.valueOf(muteToggleKey));
			properties.setProperty(MUTED_KEY, String.valueOf(muted));
			properties.setProperty(VOICE_CHAT_KEY, String.valueOf(voiceChatEnabled));
			properties.setProperty(GAIN_KEY, String.valueOf(microphoneGain));
			properties.setProperty(VOICE_OUTPUT_VOLUME_KEY, String.valueOf(voiceOutputVolume));
			// Write to a temp file then atomically swap it in, so a crash or a second
			// writer mid-store can never leave a half-truncated config behind.
			File tmp = parent == null ? new File(CONFIG_FILE + ".tmp") : new File(parent, CONFIG_FILE + ".tmp");
			try (FileOutputStream stream = new FileOutputStream(tmp)) {
				properties.store(stream, "ChickenNugget Extras client audio settings");
			} catch (IOException ignored) {
				return;
			}
			try {
				java.nio.file.Files.move(tmp.toPath(), file.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING, java.nio.file.StandardCopyOption.ATOMIC_MOVE);
			} catch (IOException atomicUnsupported) {
				try {
					java.nio.file.Files.move(tmp.toPath(), file.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException ignored) {
				}
			}
		}
	}

	private static File configFile() {
		Minecraft minecraft = Minecraft.getInstance();
		File gameDir = minecraft == null ? new File(".") : minecraft.gameDirectory;
		return new File(new File(gameDir, "config"), CONFIG_FILE);
	}

	private static String abbreviate(String text) {
		String value = text == null || text.isBlank() ? DEFAULT_DEVICE : text;
		return value.length() <= 36 ? value : value.substring(0, 33) + "...";
	}

	private record RecordedClip(byte[] data, float sampleRate, int channels) {
	}
}
