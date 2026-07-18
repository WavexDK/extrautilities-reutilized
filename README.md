# Extra Utilities: ReUtilized
EU:RU is a modern remake of the classic 1.12.2 mod "Extra Utilities 2" for Forge by RWTema. We have no affiliation or contact with RWTema, and EU:RU is a completely separate project that aims to recreate many features from the original mod without using any of its code, textures or assets.

## What is this?
Extra Utilities: ReUtilized is a highly configurable modern remake of the old-but-gold "Extra Utilities 2" by RWTema, bringing new features, detailed configuration, performance improvements, completely revamped textures, animations, models and more!
> Extra Utilities: ReUtilized requires GeckoLib 4 as of beta v0.1.0 on both the server and the client
> You are free to distribute, fork and mirror this mod under the GPLv3 license
> You are free to use this mod in any modpack under the GPLv3 license

Extra Utilities: ReUtilized is a 1.21.1 mod for NeoForge. It will most likely not be updated to a newer version unless demand calls for it.

## Well, what can I do?
That's a great question!

The most prominent features in this mod are the Grid Power system, the Generators, the Machines and the Rings.

### How do I get started?

It's simple, really.

Your first goal should be getting a Survival Generator, allowing you to generate FE very early while keeping progression fair and grounded. It can be crafted with a furnace, iron, redstone and cobblestone, generates 5FE per tick, and uses almost anything that can be used as fuel.

The next big step is the Resonator, which allows you to make Speed Upgrades and fundamental building blocks for higher-tier machines and generators, such as Stoneburnt. The Resonator requires Grid Power, which you can generate early-game using Solar Panels made from Lapis Lazuli, Ender Shards and Smooth Stone. Solar Panels generate 1GP each during the daytime while they have direct access to the sky.

From there, the sky's the limit. Build machines to automate materials, make better generators for more FE and GP, expand your EU:RU kingdom and achieve flight with the Angel Ring.

### Grid Power
Grid Power is a cross-dimensional, wire-free, non-shareable power system that works alongside the FE power system included with NeoForge.

Grid Power adds a meaningful new cost to machines and upgrades without making the system overly complicated.

Every player has their own Grid Power pool, which cannot be shared and is used by the machines they place. Machines and generators will stop working if their owner goes offline, as they cannot reliably check if the owner has enough GP.

Machines require Grid Power to function, while also requiring FE. Generators work without GP by default, but require it when Speed Upgrades are installed.
> Machines usually need anywhere between 8 and 16 Grid Power to function
> Speed Upgrades increase the Grid Power used by Machines and Generators
> Your Machines and Generators will **not** function if you are using more Grid Power than you have available

Generators require Grid Power only if they have upgrades, and will only generate FE if you have enough Grid Power available.

Grid Power works across dimensions as long as its sources are loaded. The Chunk Loading Ward can keep its current chunk loaded for 8GP, but only while its owner is online.

Well now, you ask, how do I acquire Grid Power? Well, it's actually pretty simple.
There are several blocks that generate Grid Power either passively or manually. The most common methods are as follows:
> Solar Panels
>  ^- Generates 1 Grid Power per block during the daytime with clear access to the sky
> 
> Lunar Panels
>  ^- Generates 0.7 Grid Power per block during the nighttime with clear access to the sky
> 
> Manual Mill
>  ^- Generates 15 Grid Power while you are actively right-clicking it, and stops when you stop right-clicking it
>
> Other Mills
> ^- Generates anywhere from 4-16GP per block depending on their requirements, such as the Water Mill needing flowing water around it

All the Grid Power you generate is added to one pool, known as your `Total Grid Power`. Machines  and generators automatically add their GP cost to your `Used Grid Power` pool without needing cables.

If your `Used Grid Power` rises above your `Total Grid Power`, all your machines and generators will stop until you generate more Grid Power or turn some machines off.

To see your current Grid Power pools, hover your mouse over a Resonating Redstone Crystal or look at any EU:RU block that requires or generates GP.

### Machines
This mod brings several new machines to the game, including the Crusher, Electric Furnace, Resonator and Enchanter. Each machine serves a purpose in the mod and supports recipe viewers such as JEI and EMI.

Some machines have a crucial purpose in progression, such as;
> The Resonator;
> ^- Allows you to make Stoneburnt from Smooth Stone, which is used to craft many generators and machines, along with Upgrades, decorative blocks and more
>  
> The Enchanter;
> ^- Allows you to upgrade some ingots and items into Enchanted versions, along with better Upgrades

The machines in this mod are still being worked on, so expect changes over the next few weeks and months.

### Generators
Generators allow for quick and easy FE generation and are fully configurable, even down to how long each fuel item burns.

Generators usually work out-of-the-box, however somewhat slowly. The first generator you will most likely make is the `Survival Generator`, which burns almost any flammable item or vanilla fuel and generates 5FE per tick, making it a good starting point with room for upgrades.

Each generator serves a different purpose, generating FE from wildly different sources ranging from Slime, to Coal, to Ender Pearls and even Nether Stars.

You can add Upgrades to any Generator to increase how much FE it generates per tick while keeping the total FE generated from each fuel item the same. Each Upgrade uses 1GP, with higher tiers allowing up to 64 Upgrades.

You can add custom fuels to Generators through the config. Custom fuels require an additional recipe file to appear in JEI.

### Rings
TBD.
