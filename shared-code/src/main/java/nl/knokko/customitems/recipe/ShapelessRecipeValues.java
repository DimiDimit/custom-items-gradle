package nl.knokko.customitems.recipe;

import nl.knokko.customitems.encoding.RecipeEncoding;
import nl.knokko.customitems.itemset.CraftingRecipeReference;
import nl.knokko.customitems.itemset.SItemSet;
import nl.knokko.customitems.model.Mutability;
import nl.knokko.customitems.recipe.ingredient.SIngredient;
import nl.knokko.customitems.recipe.ingredient.SNoIngredient;
import nl.knokko.customitems.recipe.result.SResult;
import nl.knokko.customitems.trouble.UnknownEncodingException;
import nl.knokko.customitems.util.Checks;
import nl.knokko.customitems.util.ProgrammingValidationException;
import nl.knokko.customitems.util.Validation;
import nl.knokko.customitems.util.ValidationException;
import nl.knokko.util.bits.BitInput;
import nl.knokko.util.bits.BitOutput;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ShapelessRecipeValues extends CraftingRecipeValues {

    static ShapelessRecipeValues load(
            BitInput input, byte encoding, SItemSet itemSet
    ) throws UnknownEncodingException {
        ShapelessRecipeValues result = new ShapelessRecipeValues(false);

        if (encoding == RecipeEncoding.SHAPELESS_RECIPE) {
            result.load1(input, itemSet);
        } else {
            throw new UnknownEncodingException("ShapelessCraftingRecipe", encoding);
        }

        return result;
    }

    private Collection<SIngredient> ingredients;

    public ShapelessRecipeValues(boolean mutable) {
        super(mutable);

        this.ingredients = new ArrayList<>();
    }

    public ShapelessRecipeValues(ShapelessRecipeValues toCopy, boolean mutable) {
        super(toCopy, mutable);

        this.ingredients = toCopy.getIngredients();
    }

    private void load1(BitInput input, SItemSet itemSet) throws UnknownEncodingException {
        this.result = SResult.load(input, itemSet);

        int numIngredients = (int) input.readNumber((byte) 4, false);
        this.ingredients = new ArrayList<>(numIngredients);
        for (int counter = 0; counter < numIngredients; counter++) {
            this.ingredients.add(SIngredient.load(input, itemSet));
        }
    }

    @Override
    public void save(BitOutput output) {
        output.addByte(RecipeEncoding.SHAPELESS_RECIPE);
        result.save(output);
        output.addNumber(ingredients.size(), (byte) 4, false);
        for (SIngredient ingredient : ingredients) {
            ingredient.save(output);
        }
    }

    @Override
    public ShapelessRecipeValues copy(boolean mutable) {
        return new ShapelessRecipeValues(this, mutable);
    }

    public Collection<SIngredient> getIngredients() {
        return new ArrayList<>(ingredients);
    }

    public void setIngredients(Collection<SIngredient> newIngredients) {
        assertMutable();
        Checks.nonNull(newIngredients);
        this.ingredients = Mutability.createDeepCopy(newIngredients, false);
    }

    @Override
    public void validate(SItemSet itemSet, CraftingRecipeReference selfReference) throws ValidationException, ProgrammingValidationException {
        super.validate(itemSet, selfReference);

        if (ingredients == null) throw new ProgrammingValidationException("No ingredients");
        int ingredientIndex = 0;
        for (SIngredient ingredient : ingredients) {
            ingredientIndex++;

            if (ingredient == null) throw new ProgrammingValidationException("Missing ingredient " + ingredientIndex);
            if (ingredient instanceof SNoIngredient) throw new ProgrammingValidationException("Ingredient " + ingredientIndex + " is empty");
            Validation.scope("Ingredient " + ingredientIndex, () -> ingredient.validateComplete(itemSet));
        }

        if (ingredients.isEmpty()) {
            throw new ValidationException("You need at least 1 ingredient");
        }

        for (CraftingRecipeReference otherReference : itemSet.getCraftingRecipeReferences().collect(Collectors.toList())) {
            if (selfReference == null || !selfReference.equals(otherReference)) {
                CraftingRecipeValues otherRecipe = otherReference.get();
                if (otherRecipe instanceof ShapelessRecipeValues) {

                    Collection<SIngredient> otherIngredients = ((ShapelessRecipeValues) otherRecipe).ingredients;

                    if (otherIngredients.size() == this.ingredients.size()) {
                        int size = this.ingredients.size();
                        List<SIngredient> ownIngredientList = new ArrayList<>(this.ingredients);
                        List<SIngredient> otherIngredientList = new ArrayList<>(otherIngredients);

                        int[] ownConflicts = new int[size];
                        int[] otherConflicts = new int[size];
                        for (int ownIndex = 0; ownIndex < size; ownIndex++) {
                            for (int otherIndex = 0; otherIndex < size; otherIndex++) {
                                if (ownIngredientList.get(ownIndex).conflictsWith(otherIngredientList.get(otherIndex))) {
                                    ownConflicts[ownIndex] += 1;
                                    otherConflicts[otherIndex] += 1;
                                }
                            }
                        }

                        boolean conflicts = true;
                        outerLoop:
                        for (int ownIndex = 0; ownIndex < size; ownIndex++) {
                            if (ownConflicts[ownIndex] == 0) {
                                conflicts = false;
                                break;
                            }

                            for (int otherIndex = 0; otherIndex < size; otherIndex++) {
                                if (ownIngredientList.get(ownIndex).conflictsWith(otherIngredientList.get(otherIndex))) {
                                    if (otherConflicts[otherIndex] != ownConflicts[ownIndex]) {
                                        conflicts = false;
                                        break outerLoop;
                                    }
                                }
                            }
                        }

                        if (conflicts) {
                            throw new ValidationException("Conflicts with recipe for " + otherRecipe.getResult());
                        }

                        // TODO This could use unit tests
                    }
                }
            }
        }
    }
}
