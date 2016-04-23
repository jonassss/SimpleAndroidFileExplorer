package searchUtil;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class FileSearchImpl implements FileSearch{
    private OnSearchResult onSearchResult;
    private AsyncTask asyncSearch = null;

    public FileSearchImpl(Context context){
        if (context instanceof OnSearchResult) {
            onSearchResult = (OnSearchResult) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement SearchInteractionListener");
        }
    }

    public List<String> inFolderList(String folder){
        File f = new File(folder);

        List<String> names = new ArrayList<>();
        if(f.listFiles() == null)
            return names;
        else if(f.listFiles().length == 0)
            return names;

        names = new ArrayList<>();
        for(File file : f.listFiles()){
            names.add(file.toString());
        }
        return names;
    }

    public void fileSearch(String fromFolder, String searchedString) {
        if(searchedString == null){
            Log.i("FileExplorer", "Cannot search for null!");
            return;
        }else if(searchedString.length() < 2){
            Log.i("FileExplorer", "Searched string too short");
            return;
        }
        asyncSearch = new SearchTask().execute(fromFolder, searchedString);
    }

    public void stopSearch(){
        if(asyncSearch == null) {
            return;
        }else if(!asyncSearch.isCancelled()) {
            asyncSearch.cancel(true);
        }
    }

    // Used to push search results before the whole search finishes
    public interface OnSearchResult{
        void onPatternMatch(String result);
    }

    private class SearchTask extends AsyncTask<String, Void, Void>{
        @Override
        protected Void doInBackground(String... params) {
            File currentDir = new File(params[0]);
            if (currentDir.isDirectory()) {
                search(currentDir, Pattern.compile(params[1].toLowerCase()));
            } else {
                Log.i("FileExplorer", currentDir.getAbsoluteFile() + " is not a directory!");
            }
            return null;
        }
        private void search(File file, Pattern searchedFile) {
            if(this.isCancelled()){
                return;
            }else if (file.canRead() && !file.isHidden()) {
                for (File temp : file.listFiles()) {
                    if (searchedFile.matcher(temp.getName().toLowerCase()).matches()) {
                        onSearchResult.onPatternMatch(temp.getAbsoluteFile().toString());
                    }
                    if (temp.isDirectory()) {
                        search(temp, searchedFile);
                    }
                }
            }
        }
    }
}
