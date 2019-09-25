package br.com.nutrifood.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import br.com.nutrifood.R;
import br.com.nutrifood.model.Alimentos;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterPesquisa extends RecyclerView.Adapter<AdapterPesquisa.MyViewHolder>{

    private List<Alimentos> alimentos;
    private Context context;

    public AdapterPesquisa(List<Alimentos> listaAlimentos, Context c ) {

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
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        try{
            Alimentos ali = alimentos.get(i);

            myViewHolder.nome.setText(ali.getNome());
            myViewHolder.descricao.setText(ali.getTipo());

            if(ali.getCaminho_Foto() != null){
                Uri uri = Uri.parse(ali.getCaminho_Foto());
                Glide.with(context).load(uri).into(myViewHolder.foto);
            }else {
                myViewHolder.foto.setImageResource(R.drawable.colher);
            }
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
        TextView nome, descricao;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            foto = itemView.findViewById(R.id.imageViewAlimento);
            nome = itemView.findViewById(R.id.txtNomeAlimento);
            descricao = itemView.findViewById(R.id.txtDescricaoAlimento);

        }
    }

}
