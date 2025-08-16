package me.nd.upgrades.model;

public class FactionModel
{
    private String tag;
    private Integer poder;
    private Integer membros;
    private Integer mobs;
    private Integer moedas;
    private Integer voar;
    private Integer plantacao;
    private Integer blocos;
    private Integer attack;
    private Integer defesas;
    private Integer ally;
    
    FactionModel(String tag, Integer poder, Integer membros, Integer mobs, Integer moedas, Integer voar, Integer plantacao, Integer blocos, Integer attack, Integer defesas, Integer ally) {
        this.tag = tag;
        this.poder = poder;
        this.membros = membros;
        this.mobs = mobs;
        this.moedas = moedas;
        this.voar = voar;
        this.plantacao = plantacao;
        this.blocos = blocos;
        this.attack = attack;
        this.defesas = defesas;
        this.ally = ally;
    }
    
    public static FactionModelBuilder builder() {
        return new FactionModelBuilder();
    }

    public String getTag() {
        return this.tag;
    }

    public Integer getPoder() {
        return this.poder;
    }

    public Integer getMembros() {
        return this.membros;
    }

    public Integer getMobs() {
        return this.mobs;
    }

    public Integer getMoedas() {
        return this.moedas;
    }

    public Integer getVoar() {
        return this.voar;
    }

    public Integer getPlantacao() {
        return this.plantacao;
    }
    
    public Integer getBlocos() {
        return this.blocos;
    }
    
    public Integer getAttack() {
        return this.attack;
    }
    
    public Integer getDefesas() {
        return this.defesas;
    }
    
    public Integer getAlly() {
        return this.ally;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setPoder(Integer poder) {
        this.poder = poder;
    }

    public void setMembros(Integer membros) {
        this.membros = membros;
    }

    public void setMobs(Integer mobs) {
        this.mobs = mobs;
    }

    public void setMoedas(Integer moedas) {
        this.moedas = moedas;
    }

    public void setVoar(Integer voar) {
        this.voar = voar;
    }

    public void setPlantacao(Integer plantacao) {
        this.plantacao = plantacao;
    }

    public void setBlocos(Integer blocos) {
        this.blocos = blocos;
    }
    
    public void setAttack(Integer attack) {
        this.attack = attack;
    }
    
    public void setDefesas(Integer defesas) {
        this.defesas = defesas;
    }
    
    public void setAlly(Integer ally) {
        this.ally = ally;
    }
    
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FactionModel)) {
            return false;
        }
        FactionModel other = (FactionModel)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$poder = this.getPoder();
        Integer other$poder = other.getPoder();
        if (this$poder == null ? other$poder != null : !((Object)this$poder).equals(other$poder)) {
            return false;
        }
        Integer this$membros = this.getMembros();
        Integer other$membros = other.getMembros();
        if (this$membros == null ? other$membros != null : !((Object)this$membros).equals(other$membros)) {
            return false;
        }
        Integer this$mobs = this.getMobs();
        Integer other$mobs = other.getMobs();
        if (this$mobs == null ? other$mobs != null : !((Object)this$mobs).equals(other$mobs)) {
            return false;
        }
        Integer this$moedas = this.getMoedas();
        Integer other$moedas = other.getMoedas();
        if (this$moedas == null ? other$moedas != null : !((Object)this$moedas).equals(other$moedas)) {
            return false;
        }
        Integer this$voar = this.getVoar();
        Integer other$voar = other.getVoar();
        if (this$voar == null ? other$voar != null : !((Object)this$voar).equals(other$voar)) {
            return false;
        }
        Integer this$plantacao = this.getPlantacao();
        Integer other$plantacao = other.getPlantacao();
        if (this$plantacao == null ? other$plantacao != null : !((Object)this$plantacao).equals(other$plantacao)) {
            return false;
        }
        Integer this$blocos = this.getBlocos();
        Integer other$blocos = other.getBlocos();
        if (this$blocos == null ? other$blocos != null : !((Object)this$blocos).equals(other$blocos)) {
            return false;
        }
        Integer this$attack = this.getAttack();
        Integer other$attack = other.getAttack();
        if (this$attack == null ? other$attack != null : !((Object)this$attack).equals(other$attack)) {
            return false;
        }
        
        Integer this$defesas = this.getDefesas();
        Integer other$defesas = other.getDefesas();
        if (this$defesas == null ? other$defesas != null : !((Object)this$defesas).equals(other$defesas)) {
            return false;
        }
        
        Integer this$ally = this.getAlly();
        Integer other$ally = other.getAlly();
        if (this$ally == null ? other$ally != null : !((Object)this$ally).equals(other$ally)) {
            return false;
        }
        
        String this$tag = this.getTag();
        String other$tag = other.getTag();
        return !(this$tag == null ? other$tag != null : !this$tag.equals(other$tag));
    }

    protected boolean canEqual(Object other) {
        return other instanceof FactionModel;
    }

	public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $poder = this.getPoder();
        result = result * PRIME + ($poder == null ? 43 : ((Object)$poder).hashCode());
        Integer $membros = this.getMembros();
        result = result * PRIME + ($membros == null ? 43 : ((Object)$membros).hashCode());
        Integer $mobs = this.getMobs();
        result = result * PRIME + ($mobs == null ? 43 : ((Object)$mobs).hashCode());
        Integer $moedas = this.getMoedas();
        result = result * PRIME + ($moedas == null ? 43 : ((Object)$moedas).hashCode());
        Integer $voar = this.getVoar();
        result = result * PRIME + ($voar == null ? 43 : ((Object)$voar).hashCode());
        Integer $plantacao = this.getPlantacao();
        result = result * PRIME + ($plantacao == null ? 43 : ((Object)$plantacao).hashCode());
        Integer $blocos = this.getBlocos();
        result = result * PRIME + ($blocos == null ? 43 : ((Object)$blocos).hashCode());
        Integer $attack = this.getAttack();
        result = result * PRIME + ($attack == null ? 43 : ((Object)$attack).hashCode());
        Integer $defesas = this.getDefesas();
        result = result * PRIME + ($defesas == null ? 43 : ((Object)$defesas).hashCode());
        Integer $ally = this.getAlly();
        result = result * PRIME + ($ally == null ? 43 : ((Object)$ally).hashCode());
        String $tag = this.getTag();
        result = result * PRIME + ($tag == null ? 43 : $tag.hashCode());
        return result;
    }

    public String toString() {
        return "FactionModel(tag=" + this.getTag() + ", poder=" + this.getPoder() + ", membros=" + this.getMembros() + ", mobs=" + this.getMobs() + ", moedas=" + this.getMoedas() + ", voar=" + this.getVoar() + ", plantacao=" + this.getPlantacao() + ", blocos=" + this.getBlocos() + ", attack=" + this.getAttack() + ", defesas=" + this.getDefesas() + ", ally=" + this.getAlly() + ")";
    }

    public static class FactionModelBuilder {
        private String tag;
        private Integer poder;
        private Integer membros;
        private Integer mobs;
        private Integer moedas;
        private Integer voar;
        private Integer plantacao;
        private Integer blocos;
        private Integer attack;
        private Integer defesas;
        private Integer ally;

        FactionModelBuilder() {
        }

        public FactionModelBuilder tag(String tag) {
            this.tag = tag;
            return this;
        }

        public FactionModelBuilder poder(Integer poder) {
            this.poder = poder;
            return this;
        }

        public FactionModelBuilder membros(Integer membros) {
            this.membros = membros;
            return this;
        }

        public FactionModelBuilder mobs(Integer mobs) {
            this.mobs = mobs;
            return this;
        }

        public FactionModelBuilder moedas(Integer moedas) {
            this.moedas = moedas;
            return this;
        }

        public FactionModelBuilder voar(Integer voar) {
            this.voar = voar;
            return this;
        }

        public FactionModelBuilder plantacao(Integer plantacao) {
            this.plantacao = plantacao;
            return this;
        }
        public FactionModelBuilder blocos(Integer blocos) {
            this.blocos = blocos;
            return this;
        }
        
        public FactionModelBuilder attack(Integer attack) {
            this.attack = attack;
            return this;
        }
        
        public FactionModelBuilder defesas(Integer defesas) {
            this.defesas = defesas;
            return this;
        }
        
        public FactionModelBuilder ally(Integer ally) {
            this.ally = ally;
            return this;
        }
        
        public FactionModel build() {
            return new FactionModel(this.tag, this.poder, this.membros, this.mobs, this.moedas, this.voar, this.plantacao, this.blocos, this.attack, this.defesas, this.ally);
        }

        public String toString() {
            return "FactionModel.FactionModelBuilder(tag=" + this.tag + ", poder=" + this.poder + ", membros=" + this.membros + ", mobs=" + this.mobs + ", moedas=" + this.moedas + ", voar=" + this.voar + ", plantacao=" + this.plantacao + ", blocos=" + this.blocos + ", attack=" + this.attack + ", defesas=" + this.defesas + ", ally=" + this.ally + ")";
        }
    }

}
