package searchUtil;

import java.util.List;

/**
 * Created by jonasfetkar on 20/04/16.
 */
public interface FileSearch {
    List<String> inFolderList(String folder);
    void fileSearch(String currentDir, String filename); // pushes results on find through "OnSearchResult" interface
    void stopSearch();
}
