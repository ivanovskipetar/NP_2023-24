package FirstPartialExercises.IFile;
import java.util.*;
import java.util.stream.Collectors;

class FileNameExistsException extends Exception {
    public FileNameExistsException(String message) {
        super(message);
    }
}

interface IFile extends Comparable<IFile> {
    String getFileName();

    long getFileSize();

    String getFileInfo(String indent);

    void sortBySize();

    long findLargestFile();

    @Override
    default int compareTo(IFile other) {
        return Long.compare(other.getFileSize(),this.getFileSize());
    }
}

class File implements IFile {
    String name;
    long size;

    public File(String name, long size) {
        this.name = name;
        this.size = size;
    }

    @Override
    public String getFileName() {
        return name;
    }

    @Override
    public long getFileSize() {
        return size;
    }

    @Override
    public String getFileInfo(String indent) {
        return String.format("%sFile name: %10s File size: %10d", indent, getFileName(), getFileSize());
    }

    @Override
    public void sortBySize() {

    }

    @Override
    public long findLargestFile() {
        return size;
    }
}

class Folder extends File {

    List<IFile> files;

    public Folder(String name) {
        super(name, 0);
        this.files = new ArrayList<>();
    }

    public void addFile(IFile file) throws FileNameExistsException {
        for (IFile ifile : files) {
            if (ifile.getFileName().equals(file.getFileName()))
                throw new FileNameExistsException(String.format("There is already a file named %s in the folder %s", ifile.getFileName(), this.name));
        }
        files.add(file);
    }

    @Override
    public long getFileSize() {
        return files.stream()
                .mapToLong(f -> f.getFileSize())
                .sum();
    }

    @Override
    public String getFileInfo(String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%sFolder name: %10s Folder size: %10d", indent, name, getFileSize())).append("\n");

        sb.append(files.stream()
                .map(file -> file.getFileInfo(indent + "    "))
                .collect(Collectors.joining("\n")));

        return sb.toString();
    }

    @Override
    public void sortBySize() {
        files.sort(Comparator.naturalOrder());
    }

    @Override
    public long findLargestFile() {
        return files.stream()
                .mapToLong(f -> f.findLargestFile())
                .max()
                .orElse(0);
    }
}

class FileSystem {

    Folder rootDirectory;

    public FileSystem() {
        this.rootDirectory = new Folder("root");
    }

    public void addFile(IFile file) throws FileNameExistsException {
        rootDirectory.addFile(file);
    }

    public void sortBySize() {
        rootDirectory.sortBySize();
    }

    public long findLargestFile() {
        return rootDirectory.findLargestFile();
    }

    @Override
    public String toString() {
        return rootDirectory.getFileInfo("");
    }
}
public class FileSystemTest {

    public static Folder readFolder(Scanner sc) {

        Folder folder = new Folder(sc.nextLine());
        int totalFiles = Integer.parseInt(sc.nextLine());

        for (int i = 0; i < totalFiles; i++) {
            String line = sc.nextLine();

            if (line.startsWith("0")) {
                String fileInfo = sc.nextLine();
                String[] parts = fileInfo.split("\\s+");
                try {
                    folder.addFile(new File(parts[0], Long.parseLong(parts[1])));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                try {
                    folder.addFile(readFolder(sc));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        return folder;
    }

    public static void main(String[] args) {

        //file reading from input

        Scanner sc = new Scanner(System.in);

        System.out.println("===READING FILES FROM INPUT===");
        FileSystem fileSystem = new FileSystem();
        try {
            fileSystem.addFile(readFolder(sc));
        } catch (FileNameExistsException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("===PRINTING FILE SYSTEM INFO===");
        System.out.println(fileSystem.toString());
        System.out.println();

        System.out.println("===PRINTING FILE SYSTEM INFO AFTER SORTING===");
        fileSystem.sortBySize();
        System.out.println(fileSystem.toString());
        System.out.println();

        System.out.println("===PRINTING THE SIZE OF THE LARGEST FILE IN THE FILE SYSTEM===");
        System.out.println(fileSystem.findLargestFile());


    }
}


