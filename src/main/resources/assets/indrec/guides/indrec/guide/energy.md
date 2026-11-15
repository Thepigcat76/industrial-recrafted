---
navigation:
  title: Energy
  icon: "indrec:copper_cable"
  position: 1
item_ids:
  - indrec:tin_cable
  - indrec:copper_cable
  - indrec:gold_cable
  - indrec:hv_cable
  - indrec:glass_fibre_cable
  
  - indrec:battery_box
  - indrec:basic_energy_storage_unit
  - indrec:advanced_energy_storage_unit

  - indrec:redstone_battery
  - indrec:energy_crystal
  - indrec:lapotron_crystal
  
  - indrec:energy_upgrade
  - indrec:transformer_upgrade
  
  - indrec:low_transformer
  - indrec:medium_transformer
  - indrec:high_transformer
  - indrec:extreme_transformer
  
  - indrec:basic_generator
  - indrec:basic_solar_panel
  - indrec:geothermal_generator
  - indrec:water_mill
  - indrec:wind_mill
  - indrec:nuclear_reactor
---

# Energy

Energy in Industrial Recrafted is used to power **Machines**, **Electric Tools** and **Armor**. Energy is very similar to most technology mods using energy units like FE. The main difference is **Energy Tiers**.

## Generating Energy

<Row>
    <ItemImage id="indrec:basic_generator" scale="3" />
    <ItemImage id="indrec:basic_solar_panel" scale="3" />
    <ItemImage id="indrec:geothermal_generator" scale="3" />
    <ItemImage id="indrec:water_mill" scale="3" />
    <ItemImage id="indrec:wind_mill" scale="3" />
    <ItemImage id="indrec:nuclear_reactor" scale="3" />
</Row>

Energy can be generated using generators. The most basic of which is the <ItemImage id="indrec:basic_generator" scale="0.5" /> Basic Generator, that generates energy from burnable fuels, similar to a furnace.
The Basic Generator is required to craft all the other generators.

- <ItemImage id="indrec:basic_solar_panel" scale="0.5" /> Solar Panel - Generates energy from the sun, only works at day, when exposed to the sky
- <ItemImage id="indrec:geothermal_generator" scale="0.5" /> Geothermal Generator - Generates energy from hot fluids like Lava
- <ItemImage id="indrec:water_mill" scale="0.5" /> Water Mill - Place the wooden wheel in flowing water, passively generates small amounts of energy
- <ItemImage id="indrec:wind_mill" scale="0.5" /> Wind Mill - Generates energy from wind, place it farther up to generate more energy
- <ItemImage id="indrec:nuclear_reactor" scale="0.5" /> Nuclear Reactor - Generates energy through nuclear fission reaction [WIP]

Each generator generates energy at a specific tier, indicated by the generator's tooltip.

## Storing Energy

<Row>
    <ItemImage id="indrec:redstone_battery" scale="3" />
    <ItemImage id="indrec:energy_crystal" scale="3" />
    <ItemImage id="indrec:lapotron_crystal" scale="3" />
    <ItemImage id="minecraft:air" scale="3" />
    <ItemImage id="indrec:battery_box" scale="3" />
    <ItemImage id="indrec:basic_energy_storage_unit" scale="3" />
    <ItemImage id="indrec:advanced_energy_storage_unit" scale="3" />
</Row>

Energy can be stored in items like the <ItemImage id="indrec:redstone_battery" scale="0.5" /> Redstone Battery, <ItemImage id="indrec:energy_crystal" scale="0.5" /> Energy Crystal and <ItemImage id="indrec:lapotron_crystal" scale="0.5" /> Lapotron Crystal.
To store energy in blocks and to charge the aforementioned battery items, one can use Energy Storage Units like the <ItemImage id="indrec:battery_box" scale="0.5" /> Battery Box, <ItemImage id="indrec:basic_energy_storage_unit" scale="0.5" /> Basic Energy Storage Unit and <ItemImage id="indrec:advanced_energy_storage_unit" scale="0.5" /> Advanced Energy Storage Unit.
Like all machines each of these have an Energy Tier specified in their tooltip.

Every Energy Storage Unit has a dot on one of its sides. This is the output side. All other sides can be used to input energy to the Energy Storage Unit.
The 'plus' slot charges electric items, while the 'minus' slot discharges items.

### Energy Storage Upgrade

<ItemImage id="indrec:energy_upgrade" scale="3" />

The energy storage upgrade can be installed in most machines to increase the amount of energy a machine can hold.

## Energy Tiers

Energy Tiers are like Voltage in real life. Machines require energy of a specific Tier to work. This is indicated by the machine's tooltip. Most machines, except for Transformers have
an Energy Tier threshold, accepting one Tier above and below their specified tier as well. The <ItemImage id="indrec:canning_machine" scale="0.5" /> Canning Machine for example, is of Tier **Medium**.
This means that energy of both the **Low** Tier and the **High** Tier is accepted besides the regular **Medium** Tier energy.

If a Machine, Transformer or Energy Storage Unit is supplied with a tier that is too high, it will burn. To use the machine again, remove the energy supply and repair the burnt machine using a <ItemImage id="indrec:fuse" scale="0.5" /> Fuse.

### Converting between Energy Tiers

<Row>
    <ItemImage id="indrec:low_transformer" scale="3" />
    <ItemImage id="indrec:medium_transformer" scale="3" />
    <ItemImage id="indrec:high_transformer" scale="3" />
    <ItemImage id="indrec:extreme_transformer" scale="3" />
</Row>

Sometimes a machine requires energy with a higher or lower Tier than what you are currently storing or generating. To still be able to use this machine, one can use **Transformers** to change the Tier.
The transformer accepts only the two Tiers of energy that are specified in the tooltip.

The side with the smaller dot and the small arrow pointing towards it, inputs/outputs the low Energy Tier. The side with the large dot and large arrow pointing towards it,
inputs/outputs the high Energy Tier. If energy of a non-compatible Tier is supplied to one of the sides or energy is supplied from both sides at the same time, the Transformer will burn and needs to be repaired using a <ItemImage id="indrec:fuse" scale="0.5" /> Fuse.

#### Transformer Upgrade

<ItemImage id="indrec:transformer_upgrade" scale="3" />

A machine's Energy Tier can also be increased using a Transformer Upgrade. When installed on a machine, it increases the Tier of this machine by one. In the case of an <ItemImage id="indrec:electric_furnace" scale="0.5" /> Electric Furnace of Tier **Low**. This would mean
that the Machine's Tier with the Upgrade installed is **Medium** and can accept **Low**, **Medium** and **High** energy because of the normal energy threshold.
