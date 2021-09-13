public interface Item {
    public void play();
    public void stop();

    public void addFather(Item father);
    public void addChild(Item child);

    public void removeFather(Item father);
    public void removeChild(Item child);
}
