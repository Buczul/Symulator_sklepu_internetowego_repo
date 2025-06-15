package productPCG;

public enum BookCategory {
    BIOGRAPHY("Biografia"),
    FANTASY("Fantastyka"),
    SCIENCE_FICTION("Science Fiction"),
    HISTORICAL("Historyczne"),
    CRIME("Kryminały"),
    CHILDREN("Dla dzieci"),
    LANGUAGE_LEARNING("Nauka języków"),
    SCIENTIFIC("Naukowe"),
    POPULAR_SCIENCE("Popularnonaukowe"),
    CONTEMPORARY("Obyczajowe"),
    LITERATURE("Literatura piękna"),
    OTHER("Inne");

    private final String polishName;

    BookCategory(String polishName) {
        this.polishName = polishName;
    }

    @Override
    public String toString() {
        return polishName;
    }
}
