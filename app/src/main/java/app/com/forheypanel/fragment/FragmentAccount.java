package app.com.forheypanel.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.squareup.picasso.Picasso;

import app.com.forheypanel.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by nayram on 11/21/15.
 */
public class FragmentAccount extends Fragment {
    EditText etName,etPhone,etEmail,etRole;
    SharedPreferences preferences;
    CircleImageView imageView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.layout_account,container,false);
        etEmail=(EditText)rootView.findViewById(R.id.etEmail);
        etName=(EditText)rootView.findViewById(R.id.etName);
        etPhone=(EditText)rootView.findViewById(R.id.etPhone);
        etRole=(EditText)rootView.findViewById(R.id.etRole);
        imageView=(CircleImageView)rootView.findViewById(R.id.img_profile);
        preferences=getActivity().getSharedPreferences("Credentials", Context.MODE_PRIVATE);
        etEmail.setText(preferences.getString("email",""));
        etName.setText(preferences.getString("name",""));
        String role=preferences.getString("role","");

        if (role.contains("heygirl")){
            etRole.setText("Heygirl");
        }

        String image=preferences.getString("image","");
        if (!image.isEmpty())
        Picasso.with(getActivity())
                .load(image)
                .placeholder(getResources().getDrawable(R.drawable.ic_action_person)).into(imageView);

        etName.setFocusable(false);
        etEmail.setFocusable(false);
        etRole.setFocusable(false);

        return rootView;
    }
}
