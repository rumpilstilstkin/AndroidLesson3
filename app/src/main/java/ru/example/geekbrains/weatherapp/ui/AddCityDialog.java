package ru.example.geekbrains.weatherapp.ui;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.util.TypedValue;
import android.widget.EditText;

import ru.example.geekbrains.weatherapp.R;
import ru.example.geekbrains.weatherapp.ui.dialogs.AddCityDialogListener;


///////////////////////////////////////////////////////////////////////////
// Add City Dialog
///////////////////////////////////////////////////////////////////////////

public class AddCityDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.change_city_dialog));
        final EditText input = new EditText(getActivity());
        Resources r = getResources();
        int px = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, r.getDisplayMetrics());
        input.setPadding(px, px, px, px);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton(R.string.label_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((AddCityDialogListener)getActivity()).onChangeCity(input.getText().toString());
            }
        });
        return builder.create();
    }
}
