package nl.knokko.customitems.editor.resourcepack;

import nl.knokko.customitems.itemset.ItemSet;
import nl.knokko.customitems.misc.CombinedResourcepackValues;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ResourcepackCombiner {

    private final ItemSet itemSet;
    private final ZipOutputStream zipOutput;

    public ResourcepackCombiner(ItemSet itemSet, ZipOutputStream zipOutput) {
        this.itemSet = itemSet;
        this.zipOutput = zipOutput;
    }

    private void write(Stream<CombinedResourcepackValues> packs) throws IOException {
        try {
            packs.forEachOrdered(pack -> {
                try {
                    byte[] buffer = new byte[100_000];

                    ZipInputStream zipInput = new ZipInputStream(new ByteArrayInputStream(pack.getContent()));
                    ZipEntry inputEntry = zipInput.getNextEntry();
                    while (inputEntry != null) {
                        zipOutput.putNextEntry(inputEntry);

                        int numReadBytes = zipInput.read(buffer);
                        while (numReadBytes != -1) {
                            zipOutput.write(buffer, 0, numReadBytes);
                            numReadBytes = zipInput.read(buffer);
                        }

                        zipOutput.flush();
                        zipOutput.closeEntry();
                        inputEntry = zipInput.getNextEntry();
                    }
                    zipInput.close();
                } catch (IOException invalid) {
                    throw new RuntimeException(invalid);
                }
            });
        } catch (RuntimeException maybeIoFailed) {
            if (maybeIoFailed.getCause() instanceof IOException) {
                throw (IOException) maybeIoFailed.getCause();
            } else throw maybeIoFailed;
        }
    }

    public void writeLate() throws IOException {
        Stream<CombinedResourcepackValues> latePacks = itemSet.getCombinedResourcepacks().stream().filter(
                pack -> pack.getPriority() < 0
        ).sorted(Comparator.comparingInt(CombinedResourcepackValues::getPriority).reversed());

        write(latePacks);
    }

    public void writeEarly() throws IOException {
        Stream<CombinedResourcepackValues> earlyPacks = itemSet.getCombinedResourcepacks().stream().filter(
                pack -> pack.getPriority() > 0
        ).sorted(Comparator.comparingInt(CombinedResourcepackValues::getPriority).reversed());

        write(earlyPacks);
    }
}
