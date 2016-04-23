package fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.regex.Pattern;

import io.github.jonassss.fileexplorer.R;

public class FileSearchFragment extends Fragment {

    private SearchInteractionListener searchListener;

    public FileSearchFragment() {} //required

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_file_search, container, false);

        EditText editText = (EditText)v.findViewById(R.id.editText);
        setInputFilter(editText);
        setInputListener(editText);

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SearchInteractionListener) {
            searchListener = (SearchInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement SearchInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        searchListener = null;
    }

    private void setInputListener(EditText editText){
        editText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchListener.onSearchInput(s.toString());
            }
        });
    }

    private void setInputFilter(EditText editText){
        InputFilter filter = new InputFilter(){
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend)
            {
                if (source.length() < 1) return null;
                Pattern fileNamePattern = Pattern.compile("([a-zA-Z]:)?(\\\\[a-zA-Z0-9_.-]+)+\\\\?");
                char last = source.charAt(source.length() - 1);
                if(fileNamePattern.matcher(source.toString()).matches()) return source.subSequence(0, source.length() - 1);
                return null;
            }
        };
        editText.setFilters(new InputFilter[] { filter });
    }

    /**
     * ! Required interface
     */
    public interface SearchInteractionListener{
        void onSearchInput(String input);
    }
}
