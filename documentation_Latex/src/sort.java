    //Filtrowanie produktów w panelu
    private List<ProductInfo> filterProducts(List<ProductInfo> products) {
        String selectedType = (String) productTypeCombo.getSelectedItem();
        String selectedCategory = (String) categoryCombo.getSelectedItem();

        return products.stream()
                .filter(p -> selectedType.equals("Wszystkie typy") ||
                        (selectedType.equals("Książki fizyczne") && p.getProductType().equals("PHYSICAL")) ||
                        (selectedType.equals("E-booki") && p.getProductType().equals("EBOOK")) ||
                        (selectedType.equals("Audiobooki") && p.getProductType().equals("AUDIOBOOK")))
                .filter(p -> selectedCategory.equals("Wszystkie kategorie") ||
                        BookCategory.valueOf(p.getCategoryName()).toString().equals(selectedCategory))
                .collect(Collectors.toList());
    }

    //Sortowanie listy
    private void sortProducts(List<ProductInfo> products) {
        String sortBy = (String) sortByCombo.getSelectedItem();

        List<ProductInfo> mutableList = new ArrayList<>(products);

        mutableList.sort((p1, p2) -> {
            switch (sortBy) {
                case "Cena (rosnąco)":
                    return Double.compare(p1.getPrice(), p2.getPrice());
                case "Tytuł (od A do Z)":
                    return p1.getTitle().compareToIgnoreCase(p2.getTitle());
                case "Autor (od A do Z)":
                    return p1.getAuthor().compareToIgnoreCase(p2.getAuthor());
                default:
                    return 0;
            }
        });
        products.clear();
        products.addAll(mutableList);
    }