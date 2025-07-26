package BlockDynasty.BukkitImplementation.logs;

import BlockDynasty.BukkitImplementation.scheduler.ContextualTask;
import BlockDynasty.BukkitImplementation.scheduler.Scheduler;
import com.google.common.collect.Sets;
import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import BlockDynasty.BukkitImplementation.utils.UtilTime;
import BlockDynasty.Economy.domain.services.log.Log;

import java.io.*;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public abstract class AbstractLogger implements Log {
    private final BlockDynastyEconomy plugin;
    private final File folder;
    private final File latest;
    private final Set<String> toAdd;
    private volatile boolean zipping;

    /**
     * Constructor for AbstractLogger.
     *
     * @param plugin The BlockDynastyEconomy plugin instance.
     */
    public AbstractLogger(BlockDynastyEconomy plugin) {
        this.plugin = plugin;
        this.folder = new File(plugin.getDataFolder() + File.separator + "logs");
        this.latest = new File(folder, "LATEST.log");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        if (!latest.exists()) {
            try {
                latest.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        this.toAdd = Sets.newHashSet();
        this.zipping = false;

        this.save();
    }

    public void log(String message) {
        try {
            StringBuilder builder = new StringBuilder();
            appendDate(builder);
            builder.append(getName());
            builder.append(message);
            writeToFile(builder.toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public abstract String getName();

    public void save() {
        zipAndReplace();
    }
    private void zipAndReplace() {
        zipping = true;

        Scheduler.runAsync( ContextualTask.build(() -> {
            try {
                String date = UtilTime.date();
                date = date.replace("/", "-");
                File zFile = new File(folder, date + ".zip");
                int link = 1;
                while (zFile.exists()) {
                    zFile = new File(folder, (date + '[') + link + (']' + ".zip"));
                    link++;
                }
                FileOutputStream fos = new FileOutputStream(zFile);
                ZipOutputStream zipOut = new ZipOutputStream(fos);
                File fileToZip = latest;
                FileInputStream fis = new FileInputStream(fileToZip);
                ZipEntry zipEntry = new ZipEntry(date + ".log");
                zipOut.putNextEntry(zipEntry);
                final byte[] bytes = new byte[1024];
                int length;
                while ((length = fis.read(bytes)) >= 0) {
                    zipOut.write(bytes, 0, length);
                }
                zipOut.close();
                fis.close();
                fos.close();
                latest.delete();
                if (!plugin.isDisabling()) {
                    latest.createNewFile();
                    PrintWriter writer = new PrintWriter(new FileWriter(latest, true));
                    toAdd.forEach(writer::println);
                    toAdd.clear();
                    writer.close();
                }
                zipping = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
    }

    private File getLatest() {
        return latest;
    }

    private File getFolder() {
        return folder;
    }

    private void warn(String message) {
        try {
            StringBuilder builder = new StringBuilder();
            appendDate(builder);
            builder.append('[').append("WARNING").append(']').append(' ');
            builder.append(message);
            writeToFile(builder.toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void error(String message, Exception ex) {
        try {
            StringBuilder builder = new StringBuilder();
            appendDate(builder);
            StackTraceElement element = ex.getStackTrace()[0];
            builder.append('[').append(ex.toString()).append(']').append(' ');
            builder.append('[').append("ERROR - ").append(ex.getMessage()).append(" -- ").append(element.getFileName())
                    .append(" where ").append(element.getMethodName()).append(" at ").append(element.getLineNumber())
                    .append(']').append(' ');
            builder.append(message);
            writeToFile(builder.toString());
        } catch (IOException e) {
            ex.printStackTrace();
        }
    }

    private  void appendDate(StringBuilder builder) {
        builder.append('[').append(getDateAndTime()).append(']').append(' ');
    }

    private  void writeToFile(String string) throws IOException {
        if (zipping) {
            toAdd.add(string);
            return;
        }
        PrintWriter writer = new PrintWriter(new FileWriter(latest, true));
        writer.println(string);
        writer.close();
    }

    private String getDateAndTime() {
        return UtilTime.now();
    }
}
