public interface Item {
    public Object play();
    public Object stop();

    public Item getFather();
    public Item getChild();

    public void setFather(Item father);
    public void setChild(Item child);
}
