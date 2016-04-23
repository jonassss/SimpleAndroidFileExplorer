package fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.github.jonassss.fileexplorer.R;

public class FileExplorerFragment extends Fragment implements FileExplorer {
    private List<String> fileNames = new ArrayList<>();
    private FileInteraction fileInteraction;

    public FileExplorerFragment() {} // required empty constructor

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_file_explorer, container, false);

        setFolderUpListener((ImageButton)v.findViewById(R.id.upFolder));

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FileInteraction) {
            fileInteraction = (FileInteraction) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement fileInteraction");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fileInteraction = null;
    }

    public void setListingText(String text){
        if(getView() != null){
            ((TextView)getView().findViewById(R.id.currentFolder)).setText(text);
        }else{
            Log.e("FileExplorer", "Fragment view not set!");
        }
    }

    public void addFile(String fileName){
        fileNames.add(fileName);
        createList(getView());
    }

    public void setList(List<String> filenames){
        this.fileNames = filenames;
        createList(getView());
    }

    private void setFolderUpListener(ImageButton imageButton){
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileInteraction.onUpFolderClick();
            }
        });
    }

    private void createList(final View v){
        GridView listen = (GridView)v.findViewById(R.id.fileGrid);

        listen.setAdapter(new FileListAdapter(getActivity()));
        listen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File file = new File(fileNames.get(position));
                if(file.isDirectory() && file.canRead()){
                    fileInteraction.onFileClick(fileNames.get(position));
                }
            }
        });
    }

    private class FileListAdapter extends BaseAdapter{
        private Context context;

        public FileListAdapter(Context cont) {
            this.context = cont;
        }

        @Override
        public int getCount() {
            return fileNames.size();
        }

        @Override
        public Object getItem(int position) {
            return fileNames.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            LinearLayout rad;
            if(convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rad = (LinearLayout) inflater.inflate(R.layout.text_item, null);
            }else{
                rad = (LinearLayout)convertView;
            }
            File file = new File(fileNames.get(position));
            if(file.isDirectory() && file.canRead()){
                ((ImageView)rad.getChildAt(0)).setImageResource(R.drawable.ic_folder_closed);
            }else if(file.isFile()){
                ((ImageView)rad.getChildAt(0)).setImageResource(R.drawable.ic_file_wtext);
            }else{
                ((ImageView)rad.getChildAt(0)).setImageResource(R.drawable.ic_folder_locked);
            }
            ((TextView)rad.getChildAt(1)).setText(fileNames.get(position));
            return rad;
        }
    }
    /**
     * ! Required interface
     */
    public interface FileInteraction{
        void onFileClick(String fileName);
        void onUpFolderClick();
    }
}
