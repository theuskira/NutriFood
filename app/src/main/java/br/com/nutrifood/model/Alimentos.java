package br.com.nutrifood.model;

import java.io.Serializable;

public class Alimentos implements Serializable {

    private String nome, nome_Pesquisa, nome_Cientifico, quantidade, emailCriador, dataCriado, emailModificador, dataModificado, tipo, tipo_Id, caminho_Foto, umidade, energia, proteina, lipideos, colesterol,
            carboidrato, fibra_Alimentar, cinzas, calcio, magnesio,
            manganes, fosforo, ferro, sodio, potassio, cobre, zinco,
            retinol, tiamina, riboflavina, piridoxina, niacina, vitamina_C;

    public Alimentos() {
    }

    public String getNome() {
        return nome;
    }

    public String getNome_Pesquisa() {
        return nome_Pesquisa;
    }

    public String getNome_Cientifico() {
        return nome_Cientifico;
    }

    public String getQuantidade() {
        return quantidade;
    }

    public String getEmailCriador() {
        return emailCriador;
    }

    public String getDataCriado() {
        return dataCriado;
    }

    public String getEmailModificador() {
        return emailModificador;
    }

    public String getDataModificado() {
        return dataModificado;
    }

    public String getTipo() {
        return tipo;
    }

    public String getTipo_Id() {
        return tipo_Id;
    }

    public String getCaminho_Foto() {
        return caminho_Foto;
    }

    public String getUmidade() {
        return umidade;
    }

    public String getEnergia() {
        return energia;
    }

    public String getProteina() {
        return proteina;
    }

    public String getLipideos() {
        return lipideos;
    }

    public String getColesterol() {
        return colesterol;
    }

    public String getCarboidrato() {
        return carboidrato;
    }

    public String getFibra_Alimentar() {
        return fibra_Alimentar;
    }

    public String getCinzas() {
        return cinzas;
    }

    public String getCalcio() {
        return calcio;
    }

    public String getMagnesio() {
        return magnesio;
    }

    public String getManganes() {
        return manganes;
    }

    public String getFosforo() {
        return fosforo;
    }

    public String getFerro() {
        return ferro;
    }

    public String getSodio() {
        return sodio;
    }

    public String getPotassio() {
        return potassio;
    }

    public String getCobre() {
        return cobre;
    }

    public String getZinco() {
        return zinco;
    }

    public String getRetinol() {
        return retinol;
    }

    public String getTiamina() {
        return tiamina;
    }

    public String getRiboflavina() {
        return riboflavina;
    }

    public String getPiridoxina() {
        return piridoxina;
    }

    public String getNiacina() {
        return niacina;
    }

    public String getVitamina_C() {
        return vitamina_C;
    }
}
