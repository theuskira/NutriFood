package br.com.nutrifood.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.nutrifood.R;
import br.com.nutrifood.adapter.AlimentosAdapter;
import br.com.nutrifood.model.Alimentos;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListarAlimentosFragment extends Fragment {

    // TODO - Variavel SearchView
    private SearchView searchViewPesquisa;

    private RecyclerView recyclerViewListaAlimentos;
    private AlimentosAdapter adapter;
    private ArrayList<Alimentos> listaAlimentos;
    private DatabaseReference alimentosRef;
    private ValueEventListener valueEventListenerAlimentos;

    private TextView txtVazio;

    private AdView mAdView;

    // TODO - Variaveis Dialog Progress
    private AlertDialog.Builder builderProgress;
    private View viewProgress;
    private AlertDialog dialogProgress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_listar_alimentos, container, false);

        inicializarComponentes(view);

        //Configurações Iniciais
        listaAlimentos = new ArrayList<>();
        alimentosRef = FirebaseDatabase.getInstance().getReference().child("alimentos");
        alimentosRef.keepSynced(true);

        //Configurar Adapter
        adapter = new AlimentosAdapter(listaAlimentos, view.getContext());

        //Configurar RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerViewListaAlimentos.setLayoutManager(layoutManager);
        recyclerViewListaAlimentos.setHasFixedSize(true);
        recyclerViewListaAlimentos.setAdapter( adapter );

        return view;
    }

    // TODO - Iniciar Componentes
    private void inicializarComponentes(View view) {

        recyclerViewListaAlimentos = view.findViewById(R.id.list);
        txtVazio = view.findViewById(R.id.txtListaVazia);

        // TODO - Iniciar Componentes - Dialog Progress
        builderProgress = new AlertDialog.Builder(view.getContext());
        viewProgress = getLayoutInflater().inflate(R.layout.dialog_progress, null);
        builderProgress.setView(viewProgress);
        builderProgress.setCancelable(false);
        dialogProgress = builderProgress.create();
        dialogProgress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    // TODO - Iniciar AdView
    private void inicializarAdView(){
        mAdView = getActivity().findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                mAdView.setVisibility(View.VISIBLE);
            }
        });
    }

    // TODO - Funcao que Chama a Tela de Alimentos
    private void chamarTelaAlimento(int position) {

        try{

            dialogProgress.show();

            Alimentos alimentoSelecionado = listaAlimentos.get(position);

            Intent intent = new Intent(getActivity(), AlimentoFragment.class);
            intent.putExtra("alimentos", alimentoSelecionado);
            startActivity(intent);

        }catch (Exception e){
            Toast.makeText(getActivity(), getString(R.string.erro) + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    // TODO - Recupear Alimentos
    public void recuperarAlimentos(){

        listaAlimentos.clear();

        try{
            valueEventListenerAlimentos = alimentosRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    listaAlimentos.clear();

                    if(!(getActivity()).isFinishing()){
                        //show dialog
                        dialogProgress.show();
                    }

                    for( DataSnapshot dados: dataSnapshot.getChildren() ){

                        Alimentos ali = dados.getValue(Alimentos.class);
                        listaAlimentos.add(ali);

                    }

                    if(listaAlimentos.isEmpty()){
                        recyclerViewListaAlimentos.setVisibility(View.GONE);
                        txtVazio.setVisibility(View.VISIBLE);
                        dialogProgress.cancel();
                    }else {
                        recyclerViewListaAlimentos.setVisibility(View.VISIBLE);
                        txtVazio.setVisibility(View.GONE);
                        dialogProgress.cancel();
                    }

                    adapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }catch (Exception e){
            Toast.makeText(getActivity(), getString(R.string.erro) + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    // TODO - onResume
    @Override
    public void onResume() {
        super.onResume();

        recuperarAlimentos();

        inicializarAdView();

        dialogProgress.cancel();

    }

    // TODO - onStop
    @Override
    public void onStop() {
        super.onStop();

        try{
            alimentosRef.removeEventListener(valueEventListenerAlimentos);
        }catch (Exception e){
            Toast.makeText(getContext(), getString(R.string.erro) + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

}