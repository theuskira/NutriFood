package br.com.nutrifood.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import br.com.nutrifood.R;
import br.com.nutrifood.model.Alimentos;
import de.hdodenhof.circleimageview.CircleImageView;

public class AlimentosAdapter extends RecyclerView.Adapter<AlimentosAdapter.MyViewHolder> {

    private List<Alimentos> alimentos;
    private Context context;

    public AlimentosAdapter(List<Alimentos> listaAlimentos, Context c ) {

        this.alimentos = listaAlimentos;
        this.context = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemLista = null;

        try{
            itemLista = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_alimentos, viewGroup, false);
        }catch (Exception e){
            Toast.makeText(context, R.string.erro + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {

        try{
            Alimentos ali = alimentos.get(i);

            myViewHolder.nome.setText(ali.getNome());

            if(ali.getNome_Cientifico() != null){
                myViewHolder.nomeCientifico.setText(ali.getNome_Cientifico());
                myViewHolder.nomeCientifico.setVisibility(View.VISIBLE);
            }else {
                myViewHolder.nomeCientifico.setVisibility(View.GONE);
            }

            if(ali.getQuantidade() != null){

                myViewHolder.lineargramas.setVisibility(View.VISIBLE);
                myViewHolder.gramas.setText(ali.getQuantidade());

            }else {
                myViewHolder.lineargramas.setVisibility(View.GONE);
            }

            myViewHolder.descricao.setText(ali.getTipo());

            if(ali.getCaminho_Foto() != null){

                myViewHolder.foto.setVisibility(View.GONE);
                myViewHolder.progressBar.setVisibility(View.VISIBLE);

                Uri uri = Uri.parse(ali.getCaminho_Foto());
                Glide.with(context).load(uri).into(myViewHolder.foto);

                myViewHolder.progressBar.setVisibility(View.GONE);
                myViewHolder.foto.setVisibility(View.VISIBLE);

            }else {
                myViewHolder.foto.setImageResource(R.drawable.ingredients_menu);
            }

            myViewHolder.progressBar.setVisibility(View.GONE);
            myViewHolder.foto.setVisibility(View.VISIBLE);

        }catch (Exception e){
            Toast.makeText(context, R.string.erro + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public int getItemCount() {
        return alimentos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        CircleImageView foto;
        TextView nome, nomeCientifico, descricao, gramas;
        ProgressBar progressBar;
        LinearLayout lineargramas;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            foto = itemView.findViewById(R.id.imageViewAlimento);
            nome = itemView.findViewById(R.id.txtNomeAlimento);
            nomeCientifico = itemView.findViewById(R.id.txtNomeCientificoAlimento);
            descricao = itemView.findViewById(R.id.txtDescricaoAlimento);
            progressBar = itemView.findViewById(R.id.progressBarAdapter);
            lineargramas = itemView.findViewById(R.id.linearAdapterGramas);
            gramas = itemView.findViewById(R.id.txtAdapterGramas);

            // TODO - Ativar Animação

            nome.setSelected(true);
            nomeCientifico.setSelected(true);
            descricao.setSelected(true);

        }
    }

}
