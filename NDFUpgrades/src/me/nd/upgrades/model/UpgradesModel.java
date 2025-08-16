package me.nd.upgrades.model;

public class UpgradesModel
{
    private String title;
    private String type;
    private Integer level;
    private Double price;
    private Integer time;

    UpgradesModel(String title, String type, Integer level, Double price, Integer time) {
        this.title = title;
        this.type = type;
        this.level = level;
        this.price = price;
        this.time = time;
    }

    public static UpgradesModelBuilder builder() {
        return new UpgradesModelBuilder();
    }

    public String getTitle() {
        return this.title;
    }
    
    public String getType() {
        return this.type;
    }

    public Integer getLevel() {
        return this.level;
    }

    public Double getPrice() {
        return this.price;
    }
    
    public Integer getTime() {
        return this.time;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setType(String type) {
        this.type = type;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public void setPrice(Double price) {
        this.price = price;
    }    
    
    public void setTime(Integer time) {
        this.time = time;
    }   

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UpgradesModel)) {
            return false;
        }
        UpgradesModel other = (UpgradesModel)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$level = this.getLevel();
        Integer other$level = other.getLevel();
        if (this$level == null ? other$level != null : !((Object)this$level).equals(other$level)) {
            return false;
        }
        Double this$price = this.getPrice();
        Double other$price = other.getPrice();
        if (this$price == null ? other$price != null : !((Object)this$price).equals(other$price)) {
            return false;
        }
        
        Integer this$time = this.getTime();
        Integer other$time = other.getTime();
        if (this$time == null ? other$time != null : !((Object)this$time).equals(other$time)) {
            return false;
        }
        String this$title = this.getTitle();
        String other$title = other.getTitle();
        if (this$title == null ? other$title != null : !this$title.equals(other$title)) {
            return false;
        }
        String this$type = this.getType();
        String other$type = other.getType();
        return !(this$type == null ? other$type != null : !this$type.equals(other$type));
    }

    protected boolean canEqual(Object other) {
        return other instanceof UpgradesModel;
    }

    @SuppressWarnings("unused")
	public int hashCode() {
        Integer PRIME = 59;
        Integer result = 1;
        Integer $level = this.getLevel();
        result = result * 59 + ($level == null ? 43 : ((Object)$level).hashCode());
        Double $price = this.getPrice();
        result = result * 59 + ($price == null ? 43 : ((Object)$price).hashCode());
        String $title = this.getTitle();
        result = result * 59 + ($title == null ? 43 : $title.hashCode());
        String $type = this.getType();
        result = result * 59 + ($type == null ? 43 : $type.hashCode());
        Integer $time = this.getTime();
        result = result * 59 + ($time == null ? 43 : ((Object)$time).hashCode());
        return result;
    }

    public String toString() {
        return "UpgradesModel(title=" + this.getTitle() + ", type=" + getType() + ", level=" + this.getLevel() + ", price=" + this.getPrice() + ", time=" + this.getTime() + ")";
    }

    public static class UpgradesModelBuilder {
        private String title;
        private String type;
        private Integer level;
        private Double price;
        private Integer time;

        UpgradesModelBuilder() {
        	
        }

        public UpgradesModelBuilder title(String title) {
            this.title = title;
            return this;
        }

        public UpgradesModelBuilder type(String type) {
            this.type = type;
            return this;
        }

        public UpgradesModelBuilder level(Integer level) {
            this.level = level;
            return this;
        }

        public UpgradesModelBuilder price(Double price) {
            this.price = price;
            return this;
        }

        public UpgradesModelBuilder time(Integer time) {
            this.time = time;
            return this;
        }
        
        public UpgradesModel build() {
            return new UpgradesModel(this.title, type, this.level, this.price, this.time);
        }

        public String toString() {
            return "UpgradesModel.UpgradesModelBuilder(title=" + this.title + ", type=" + type + ", level=" + this.level + ", price=" + this.price + ", time=" + this.time + ")";
        }
    }
}