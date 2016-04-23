package io.github.jonassss.fileexplorer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;

import fragments.FileExplorerFragment;
import fragments.FileExplorer;
import fragments.FileSearchFragment;
import searchUtil.FileSearch;
import searchUtil.FileSearchImpl;

public class MainActivity extends AppCompatActivity implements FileExplorerFragment.FileInteraction, FileSearchFragment.SearchInteractionListener, FileSearchImpl.OnSearchResult {
    private FileSearch fileSearcher = new FileSearchImpl(this);
    private FileExplorer fileExplorer;
    private String dirPath = "/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //dirPath = Environment.getRootDirectory().getAbsolutePath();
        dirPath = getString(R.string.path_media_folder);

        List<String> fileNames = fileSearcher.inFolderList(dirPath);
        fileExplorer = (FileExplorerFragment) getSupportFragmentManager().findFragmentById(R.id.fileExplorerFragment);
        fileExplorer.setListingText(dirPath);
        fileExplorer.setList(fileNames);
    }

    @Override
    public void onFileClick(String fileName) {
        dirPath = fileName;
        List<String> fileNames = fileSearcher.inFolderList(fileName);
        fileExplorer.setListingText(fileName);
        fileExplorer.setList(fileNames);
    }

    @Override
    public void onUpFolderClick() {
        String[] path = dirPath.split("/");
        if (path.length > 0) {
            StringBuilder sb = new StringBuilder("");
            for (int i = 0; i < path.length - 1; i++) {
                sb.append(path[i]);
                sb.append("/");
            }
            dirPath = sb.toString();
            List<String> fileNames = fileSearcher.inFolderList(dirPath);
            fileExplorer.setListingText(dirPath);
            fileExplorer.setList(fileNames);
        }
    }

    @Override
    public void onSearchInput(String input) {
        fileSearcher.stopSearch(); // stop previous search

        if (dirPath == null)
            return;
        else if (dirPath.length() < 1)
            return;
        else if (input == null)
            return;
        else if (input.length() < 2) { // go back
            List<String> fileNames = fileSearcher.inFolderList(dirPath);
            fileExplorer.setListingText(dirPath);
            fileExplorer.setList(fileNames);
            return;
        }

        fileExplorer.setListingText("Searching...");
        fileExplorer.setList(new ArrayList<String>());
        fileSearcher.fileSearch(dirPath, input); // calls "onPatternMatch" on result
    }

    @Override
    public void onPatternMatch(final String result) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                fileExplorer.addFile(result);
            }
        });
    }
}