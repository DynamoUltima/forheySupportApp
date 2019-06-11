package app.com.forheypanel.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import app.com.forheypanel.R;

public class ExampleBottomDialogSheetDialog extends BottomSheetDialogFragment  implements AdapterView.OnItemSelectedListener {
    private BottomSheetListener mListener;

    TextView AmountText;
    EditText AmountEnterd;
    Button SaveButton;
    Button EditButton;
    Spinner payMode;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_layout, container, false);

        AmountText = v.findViewById(R.id.amount_text);
        AmountEnterd = v.findViewById(R.id.editTextAmount);
        payMode= v.findViewById(R.id.spinner_pay_mode);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.payment_mode, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        payMode.setAdapter(adapter);
        payMode.setOnItemSelectedListener(this);


        SaveButton = v.findViewById(R.id.save);
        EditButton = v.findViewById(R.id.edit);
        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String displayAmount = AmountEnterd.getEditableText().toString();
                AmountText.setText(displayAmount);
                mListener.onButtonClicked("Button 1 clicked");
                // dismiss();
            }
        });
        EditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onButtonClicked("Button 2 clicked");
                dismiss();
            }
        });

        return v;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public interface BottomSheetListener {
        void onButtonClicked(String text);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListener");
        }
    }
}
