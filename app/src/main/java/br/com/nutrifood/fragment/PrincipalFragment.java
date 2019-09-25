package br.com.nutrifood.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import br.com.nutrifood.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PrincipalFragment extends Fragment {

    private LinearLayout linearCalculoSimples;
    private LinearLayout linearCalculoPro;
    private LinearLayout linearAlimentos;

    CalculoFragment calculoFragment = new CalculoFragment();
    CalculoProFragment calculoProFragment = new CalculoProFragment();
    ListarAlimentosFragment listarAlimentosFragment = new ListarAlimentosFragment();

    public PrincipalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_principal, container, false);

        linearCalculoSimples = view.findViewById(R.id.linearCalculoSimples);
        linearCalculoPro = view.findViewById(R.id.linearCalculoPro);
        linearAlimentos = view.findViewById(R.id.linearAlimentos);

        linearCalculoSimples.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarFragment(calculoFragment);
            }
        });

        linearCalculoPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarFragment(calculoProFragment);
            }
        });

        linearAlimentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarFragment(listarAlimentosFragment);
            }
        });

        return view;
    }

    private void iniciarFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }



}
