---
navigation:
  title: Industrial Crops
  icon: "indrec:crop_sticks"
  position: 2
item_ids:
  - indrec:crop_sticks
  - indrec:crop_analyzer
---

# Industrial Crops

Industrial Crops are similar to normal crops but are grown on <ItemImage id="indrec:crop_sticks" scale="0.5" /> Crop Sticks and can be **cross-breed**.
They can be used to automate many resources like <ItemImage id="indrec:sticky_resin" scale="0.5" /> Sticky Resin, <ItemImage id="minecraft:iron_ingot" scale="0.5" /> Iron,
<ItemImage id="minecraft:gold_ingot" scale="0.5" /> Gold, <ItemImage id="indrec:tin_ingot" scale="0.5" /> Tin and more!

## Crop Sticks

<ItemImage id="indrec:crop_sticks" scale="3" />

Crop Sticks are used to grow Industrial Crops. They can be placed on any soil block including <ItemImage id="minecraft:farmland" scale="0.5" /> Farmland, <ItemImage id="minecraft:soul_sand" scale="0.5" /> Soul Sand and <ItemImage id="minecraft:end_stone" scale="0.5" /> End Stone (all blocks with the tag *indrec:valid_industrial_soil*).
Jumping on Farmland with Crop Sticks will not turn the Farmland back into Dirt.

Crops that can be grown on the Crop Sticks are data-driven and include all vanilla crops (besides Sniffer plants) and many more custom crops. (-> [Available Industrial Crops](crops.md#available-industrial-crops))

To plant an Industrial Crop, simply right-click its Seed item on the empty Crop Sticks. To remove a Crop from Crop Sticks, you can use <ItemImage id="minecraft:shears" scale="0.5" /> Shears. This will give you the seed or a seed drop back.
Some crops like Weed and variants of <ItemImage id="indrec:coprium" scale="0.5" /> Coprium don't have seeds and can't be planted manually. When a crop is fully grown, it can be harvested by right-clicking it with any <ItemImage id="minecraft:iron_hoe" scale="0.5" /> Hoe (when an <ItemImage id="indrec:electric_hoe" scale="0.5" /> Electric Hoe is used, energy is consumed instead of durability). If Crop Sticks are left alone for long enough, Weed will grow in them. Luckily, these weed plants will **not** spread to other Crop Sticks with
planted Crops.

Crops on Crop Sticks can be bone mealed like regular Crops, if the are planted on the correct Soil.

## Crop Requirements

By default, Crops have a Light Level Requirement of 9+ and a Soil Requirement for Farmland. However, some crops like Mushrooms, Nether Warts or Coprium for example have other Requirements.

By default, crops can have the following Requirements:

- Light Level Requirement (Specifies a minimum and maximum Light Level between 0 and 15)
- Soil Requirement (Specifies soil blocks that the crop needs to be planted on, for it to grow)
- Block Below Soil Requirement (Specify a block below the Soil that the crop requires to grow)

To see the requirements on a crop, you can use the <ItemImage id="indrec:crop_analyzer" scale="0.5" /> Crop Anaylzer.

## Crop Analyzer

<ItemImage id="indrec:crop_analyzer" scale="3" />

The Crop Analyzer is an electric item that can be used to inspect both Industrial and regular Crops and information about them.
When discovering a new Industrial Crop, you need to look at it while holding the Right Mouse Button to scan the crop and view information
about it. After it has been analyzed, information will be displayed in two tabs. You can switch between the tabs while holding Shift and scrolling.

Only analyzing the Crops costs energy, looking at them does not draw energy. The crops that have been analyzed are stored on the Crop Analyzer itself.
If you lose it, you need to analyze all crops again.

## Cross-Breeding

Cross Breeding is the main way to obtain new crops. To crossbreed two crops, place them with Crop Sticks between them. Right-Click the Crop Sticks in the middle with a
second Crop Sticks item, to convert them to Cross Breeding Mode. Then you have to wait for the crops to grow and produce an offspring. There is a 2/3 chance everytime
crossbreeding occurs, that the crop in the middle is one of the two parents. If both crops are fully grown, they will not crossbreed and need to be harvested using a Hoe first.

**All Cross Breeding Recipes can be viewed in 'Just Enough Items'.**

<GameScene zoom="4" interactive={true}>
  <ImportStructure src="structures/crossbreeding_before.snbt" />
  <BlockAnnotation x="0" y="1" z="0" color="transparent">
    Parent: Carrots
  </BlockAnnotation>
  <BlockAnnotation x="1" y="1" z="0" color="transparent">
    Cross-Breeding Crop Sticks
  </BlockAnnotation>
  <BlockAnnotation x="2" y="1" z="0" color="transparent">
    Parent: Pumpkin
  </BlockAnnotation>
</GameScene>

After some time, one of the two parent crops grows and has a chance to cross-breed. Resulting in a new crop grows on the Crop Sticks in the middle:

<GameScene zoom="4" interactive={true}>
  <ImportStructure src="structures/crossbreeding_after.snbt" />
  <BlockAnnotation x="0" y="1" z="0" color="transparent">
    Parent: Carrots
  </BlockAnnotation>
  <BlockAnnotation x="1" y="1" z="0" color="transparent">
    Result: Coprium
  </BlockAnnotation>
  <BlockAnnotation x="2" y="1" z="0" color="transparent">
    Parent: Pumpkin
  </BlockAnnotation>
</GameScene>

After cross-breeding the crop on the Crop Sticks in Cross Breeding Mode can be safely harvested using Shears. Empty Cross Breeding Crop Sticks can be
right-clicked with Shears to turn them back into regular Crop Sticks.

## Crop Interaction and Mutation

Some crops have special events when a player uses an item on them. A <ItemImage id="minecraft:poppy" scale="0.5" /> Poppy for example will turn into a
<ItemImage id="minecraft:wither_rose" scale="0.5" /> Wither Rose if right-clicked with a <ItemImage id="minecraft:potion" scale="0.5" /> Potion Of Harming.

Some crops have a chance to mutate into another Crop when they grow. In base Industrial Recrafted, this is only the case for non-waxed Coprium, which can mutate
into its oxidized forms, which do not yield any harvest. It is advised to wax your Coprium plants!

**All Crop Interactions and Mutations can be viewed in 'Just Enough Items'.**

## Crop Manager

<ItemImage id="indrec:crop_manager" scale="3" />

The Crop Manager is a machine that can be used to automatically harvest and bone-meal Industrial Crops around it. By default, it works in a 4 Block radius square from the Machine.
This means that a water source can be placed below the Crop Manager and will reach every Crop in the working area.

The Crop Manager needs a Hoe to work. If a Hoe with durability is provided, it will be damaged and will eventually break. It is therefore recommended to provide an
<ItemImage id="indrec:electric_hoe" scale="0.5" /> Electric Hoe, which will automatically be recharged by the Crop Manager. The Crop Manager also consumes a small amount of energy
per operation. It is therefore necessary to continuously provide it with energy. However, a <ItemImage id="indrec:basic_solar_panel" scale="0.5" /> Solar Panel is sufficient for this, especially when paired with an Energy Storage Unit as buffer.

Crop Drops of harvested crops are placed in the internal storage and can be extracted using a Hopper or any other method of item transportation. Bone Meal and a Hoe can be inserted using any method of item insertion.

## Available Industrial Crops

<CropList />
