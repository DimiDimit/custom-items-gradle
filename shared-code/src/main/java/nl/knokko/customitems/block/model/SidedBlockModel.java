package nl.knokko.customitems.block.model;

import nl.knokko.customitems.bithelper.BitInput;
import nl.knokko.customitems.bithelper.BitOutput;
import nl.knokko.customitems.itemset.ItemSet;
import nl.knokko.customitems.itemset.TextureReference;
import nl.knokko.customitems.trouble.UnknownEncodingException;
import nl.knokko.customitems.util.ProgrammingValidationException;
import nl.knokko.customitems.util.ValidationException;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.zip.ZipOutputStream;

public class SidedBlockModel implements BlockModel {

    static SidedBlockModel loadSided(BitInput input, ItemSet itemSet) throws UnknownEncodingException {
        byte encoding = input.readByte();
        if (encoding != 1) throw new UnknownEncodingException("SidedBlockModel", encoding);

        return new SidedBlockModel(
                itemSet.getTextureReference(input.readString()), itemSet.getTextureReference(input.readString()),
                itemSet.getTextureReference(input.readString()), itemSet.getTextureReference(input.readString()),
                itemSet.getTextureReference(input.readString()), itemSet.getTextureReference(input.readString())
        );
    }

    private final TextureReference north, east, south, west, up, down;

    public SidedBlockModel(
            TextureReference north, TextureReference east, TextureReference south, TextureReference west,
            TextureReference up, TextureReference down
    ) {
        this.north = north;
        this.east = east;
        this.south = south;
        this.west = west;
        this.up = up;
        this.down = down;
    }

    private TexturePair[] getTexturePairs() {
        TexturePair[] textures = {
                new TexturePair("north", north),
                new TexturePair("east", east),
                new TexturePair("south", south),
                new TexturePair("west", west),
                new TexturePair("up", up),
                new TexturePair("down", down)
        };
        return textures;
    }

    @Override
    public void write(ZipOutputStream zipOutput, String blockName) throws IOException {
        PrintWriter modelWriter = new PrintWriter(zipOutput);
        modelWriter.println("{");
        modelWriter.println("    \"parent\": \"block/cube\",");
        modelWriter.println("    \"textures\": {");
        for (TexturePair pair : getTexturePairs()) {
            modelWriter.print("        \"" + pair.direction + "\": \"customitems/" + pair.texture.get().getName() + "\"");
            if (!pair.direction.equals("down")) {
                modelWriter.print(",");
            }
            modelWriter.println();
        }
        modelWriter.println("    }");
        modelWriter.println("}");
        modelWriter.flush();
    }

    @Override
    public void save(BitOutput output) {
        output.addByte(MODEL_TYPE_SIDED);
        output.addByte((byte) 1);

        for (TexturePair pair : getTexturePairs()) {
            output.addString(pair.texture.get().getName());
        }
    }

    @Override
    public void validate(ItemSet itemSet) throws ValidationException, ProgrammingValidationException {
        for (TexturePair pair : getTexturePairs()) {
            if (pair.texture == null) throw new ValidationException("Missing " + pair.direction);
            if (!itemSet.isReferenceValid(pair.texture)) throw new ProgrammingValidationException(pair.direction + " is no longer valid");
        }
    }

    @Override
    public TextureReference getPrimaryTexture() {
        return north;
    }

    @Override
    public String toString() {
        return "sided";
    }

    private static class TexturePair {

        final String direction;
        final TextureReference texture;

        TexturePair(String direction, TextureReference texture) {
            this.direction = direction;
            this.texture = texture;
        }
    }
}
