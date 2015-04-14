package shiller.app;

/**
 * Created by IntelliJ IDEA.
 * User: benfa
 * Date: 14/04/12
 * Time: 19.31
 * To change this template use File | Settings | File Templates.
 */
public class Intestazione {


    private String committente;
    private String via;
    private String paese;
    private String localita;
    private String data;
    private String sistemaPerforazione;
    private String diametroPerforazione;
    private String profonditaPeroforazione;
    private String colonnaRivesetimento;
    private String dimanetroRivestimento;
    private double livelloStatico;
    private String livelloDinamico;
    private String profonditaRestrizione;
    private String portataDinamico;
    private String infoAggiuntive;
    private int profonditaPozzo;

    public Intestazione(){
        committente             =new String();
        via                     =new String();
        paese                   =new String();
        localita                =new String();
        data                    =new String();
        sistemaPerforazione     =new String();
        diametroPerforazione    =new String();
        profonditaPeroforazione =new String();
        colonnaRivesetimento    =new String();
        dimanetroRivestimento   =new String();
        livelloDinamico         =new String();
        profonditaRestrizione   =new String();
        portataDinamico         =new String();
        infoAggiuntive          =new String();

    }

    public int getProfonditaPozzo() {
        return profonditaPozzo;
    }

    public void setProfonditaPozzo(int profonditaPozzo) {
        this.profonditaPozzo = profonditaPozzo;
    }

    public String getCommittente() {
        return committente;
    }

    public void setCommittente(String committente) {
        this.committente = committente;
    }


    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }

    public String getPaese() {
        return paese;
    }

    public void setPaese(String paese) {
        this.paese = paese;
    }

    public String getLocalita() {
        return localita;
    }

    public void setLocalita(String localita) {
        this.localita = localita;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getSistemaPerforazione() {
        return sistemaPerforazione;
    }

    public void setSistemaPerforazione(String sistemaPerforazione) {
        this.sistemaPerforazione = sistemaPerforazione;
    }

    public String getDiametroPerforazione() {
        return diametroPerforazione;
    }

    public void setDiametroPerforazione(String diametroPerforazione) {
        this.diametroPerforazione = diametroPerforazione;
    }

    public String getProfonditaPeroforazione() {
        return profonditaPeroforazione;
    }

    public void setProfonditaPeroforazione(String profonditaPeroforazione) {
        this.profonditaPeroforazione = profonditaPeroforazione;
    }

    public String getColonnaRivesetimento() {
        return colonnaRivesetimento;
    }

    public void setColonnaRivesetimento(String colonnaRivesetimento) {
        this.colonnaRivesetimento = colonnaRivesetimento;
    }

    public String getDiametroRivestimento() {
        return dimanetroRivestimento;
    }

    public void setDiametroRivestimento(String dimanetroRivestimento) {
        this.dimanetroRivestimento = dimanetroRivestimento;
    }

    public double getLivelloStatico() {
        return livelloStatico;
    }

    public void setLivelloStatico(double livelloStatico) {
        this.livelloStatico = livelloStatico;
    }

    public String getLivelloDinamico() {
        return livelloDinamico;
    }

    public void setLivelloDinamico(String livelloDinamico) {
        this.livelloDinamico = livelloDinamico;
    }

    public String getProfonditaRestrizione() {
        return profonditaRestrizione;
    }

    public void setProfonditaRestrizione(String profonditaRestrizione) {
        this.profonditaRestrizione = profonditaRestrizione;
    }

    public String getPortataDinamico() {
        return portataDinamico;
    }

    public void setPortataDinamico(String portataDinamico) {
        this.portataDinamico = portataDinamico;
    }

    public String getInfoAggiuntive() {
        return infoAggiuntive;
    }

    public void setInfoAggiuntive(String infoAggiuntive) {
        this.infoAggiuntive = infoAggiuntive;
    }


}
