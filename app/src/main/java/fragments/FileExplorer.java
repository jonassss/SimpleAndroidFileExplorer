package fragments;

import java.util.List;

/**
 * Created by jonasfetkar on 20/04/16.
 */
public interface FileExplorer {
    void setList(List<String> fileNames);
    void setListingText(String text);
    void addFile(String fileName);
}