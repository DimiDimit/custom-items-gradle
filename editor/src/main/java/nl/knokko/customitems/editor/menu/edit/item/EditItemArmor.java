/*******************************************************************************
 * The MIT License
 *
 * Copyright (c) 2019 knokko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *  
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *******************************************************************************/
package nl.knokko.customitems.editor.menu.edit.item;

import nl.knokko.customitems.editor.menu.edit.EditMenu;
import nl.knokko.customitems.editor.menu.edit.EditProps;
import nl.knokko.customitems.editor.util.HelpButtons;
import nl.knokko.customitems.item.AttributeModifierValues;
import nl.knokko.customitems.item.CustomArmorValues;
import nl.knokko.customitems.item.CustomItemType;
import nl.knokko.customitems.item.DamageResistanceValues;
import nl.knokko.customitems.itemset.ItemReference;
import nl.knokko.gui.component.WrapperComponent;
import nl.knokko.gui.component.text.ConditionalTextButton;
import nl.knokko.gui.component.text.ConditionalTextComponent;
import nl.knokko.gui.component.text.EagerIntEditField;
import nl.knokko.gui.component.text.dynamic.DynamicTextButton;
import nl.knokko.gui.component.text.dynamic.DynamicTextComponent;

import java.util.function.IntConsumer;

import static nl.knokko.customitems.editor.menu.edit.EditProps.EDIT_ACTIVE;
import static nl.knokko.customitems.editor.menu.edit.EditProps.EDIT_BASE;
import static nl.knokko.customitems.item.AttributeModifierValues.*;

public class EditItemArmor<V extends CustomArmorValues> extends EditItemTool<V> {
	
	public EditItemArmor(EditMenu menu, V oldValues, ItemReference toModify) {
		super(menu, oldValues, toModify);
	}
	
	@Override
	protected AttributeModifierValues getExampleAttributeModifier() {
		double armor;
		Slot slot;
		CustomItemType i = currentValues.getItemType();
		if (i == CustomItemType.NETHERITE_HELMET) {
			armor = 3;
			slot = Slot.HEAD;
		} else if (i == CustomItemType.NETHERITE_CHESTPLATE) {
			armor = 8;
			slot = Slot.CHEST;
		} else if (i == CustomItemType.NETHERITE_LEGGINGS) {
			armor = 6;
			slot = Slot.LEGS;
		} else if (i == CustomItemType.NETHERITE_BOOTS) {
			armor = 3;
			slot = Slot.FEET;
		} else if (i == CustomItemType.DIAMOND_HELMET) {
			armor = 3;
			slot = Slot.HEAD;
		} else if (i == CustomItemType.DIAMOND_CHESTPLATE) {
			armor = 8;
			slot = Slot.CHEST;
		} else if (i == CustomItemType.DIAMOND_LEGGINGS) {
			armor = 6;
			slot = Slot.LEGS;
		} else if (i == CustomItemType.DIAMOND_BOOTS) {
			armor = 3;
			slot = Slot.FEET;
		} else if (i == CustomItemType.IRON_HELMET) {
			armor = 2;
			slot = Slot.HEAD;
		} else if (i == CustomItemType.IRON_CHESTPLATE) {
			armor = 6;
			slot = Slot.CHEST;
		} else if (i == CustomItemType.IRON_LEGGINGS) {
			armor = 5;
			slot = Slot.LEGS;
		} else if (i == CustomItemType.IRON_BOOTS) {
			armor = 2;
			slot = Slot.FEET;
		} else if (i == CustomItemType.CHAINMAIL_HELMET) {
			armor = 2;
			slot = Slot.HEAD;
		} else if (i == CustomItemType.CHAINMAIL_CHESTPLATE) {
			armor = 5;
			slot = Slot.CHEST;
		} else if (i == CustomItemType.CHAINMAIL_LEGGINGS) {
			armor = 4;
			slot = Slot.LEGS;
		} else if (i == CustomItemType.CHAINMAIL_BOOTS) {
			armor = 1;
			slot = Slot.FEET;
		} else if (i == CustomItemType.GOLD_HELMET) {
			armor = 2;
			slot = Slot.HEAD;
		} else if (i == CustomItemType.GOLD_CHESTPLATE) {
			armor = 5;
			slot = Slot.CHEST;
		} else if (i == CustomItemType.GOLD_LEGGINGS) {
			armor = 3;
			slot = Slot.LEGS;
		} else if (i == CustomItemType.GOLD_BOOTS) {
			armor = 1;
			slot = Slot.FEET;
		} else if (i == CustomItemType.LEATHER_HELMET) {
			armor = 1;
			slot = Slot.HEAD;
		} else if (i == CustomItemType.LEATHER_CHESTPLATE) {
			armor = 3;
			slot = Slot.CHEST;
		} else if (i == CustomItemType.LEATHER_LEGGINGS) {
			armor = 2;
			slot = Slot.LEGS;
		} else if (i == CustomItemType.LEATHER_BOOTS) {
			armor = 1;
			slot = Slot.FEET;
		} else if (i == CustomItemType.ELYTRA) {
			armor = 1;
			slot = Slot.CHEST;
		} else {
			throw new IllegalArgumentException("Unknown item type: " + i.name());
		}
		
		return createQuick(
				Attribute.ARMOR,
				slot,
				Operation.ADD,
				armor
		);
	}

	@Override
	protected void addComponents() {
		super.addComponents();
		addComponent(new DynamicTextComponent("Damage resistances: ", EditProps.LABEL), 0.62f, 0.35f, 0.84f, 0.425f);
		addComponent(new DynamicTextButton("Change...", EditProps.BUTTON, EditProps.HOVER, () -> {
			state.getWindow().setMainComponent(new EditDamageResistances(currentValues.getDamageResistances(), () -> {
				state.getWindow().setMainComponent(this);
			}, (DamageResistanceValues newResistances) -> {
				state.getWindow().setMainComponent(this);
				currentValues.setDamageResistances(newResistances);
			}));
		}), 0.85f, 0.35f, 0.99f, 0.425f);
		if (!(this instanceof EditItemHelmet3D || this instanceof EditItemElytra)) {
			addComponent(new ConditionalTextComponent(
					"Worn texture:", EditProps.LABEL, () -> !showColors()), 
					0.65f, 0.29f, 0.84f, 0.35f);
			addComponent(new ConditionalTextButton(
					"Change...", EditProps.BUTTON, EditProps.HOVER, () -> {
						state.getWindow().setMainComponent(new SelectWornTexture(
								this, menu.getSet(), currentValues::setArmorTexture
						));
					}, () -> !showColors()), 0.85f, 0.29f, 0.99f, 0.35f);
			addComponent(
					new ConditionalTextComponent("Red: ", EditProps.LABEL, this::showColors),
					0.78f, 0.29f, 0.84f, 0.35f
			);
			addComponent(
					new ConditionalTextComponent("Green: ", EditProps.LABEL, this::showColors),
					0.75f, 0.21f, 0.84f, 0.27f
			);
			addComponent(
					new ConditionalTextComponent("Blue: ", EditProps.LABEL, this::showColors),
					0.77f, 0.13f, 0.84f, 0.19f
			);
			addComponent(
					new ColorEditField(currentValues.getRed(), currentValues::setRed),
					0.85f, 0.28f, 0.9f, 0.35f
			);
			addComponent(
					new ColorEditField(currentValues.getGreen(), currentValues::setGreen),
					0.85f, 0.20f, 0.9f, 0.27f
			);
			addComponent(
					new ColorEditField(currentValues.getBlue(), currentValues::setBlue),
					0.85f, 0.12f, 0.9f, 0.19f
			);
		}
		errorComponent.setProperties(EditProps.LABEL);
		errorComponent.setText("Hint: Use attribute modifiers to set the armor (toughness) of this piece.");
		
		// 3d helmets are a bit different
		if (!(this instanceof EditItemHelmet3D || this instanceof EditItemElytra)) {
			HelpButtons.addHelpLink(this, "edit%20menu/items/edit/armor.html");
		}
	}
	
	private boolean showColors() {
		return currentValues.getItemType().isLeatherArmor();
	}
	
	private class ColorEditField extends WrapperComponent<EagerIntEditField> {

		public ColorEditField(int initial, IntConsumer changeValue) {
			super(new EagerIntEditField(initial, 0, 255, EDIT_BASE, EDIT_ACTIVE, changeValue));
		}
		
		@Override
		public boolean isActive() {
			return showColors();
		}
	}
}