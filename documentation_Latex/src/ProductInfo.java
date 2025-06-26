    public static class ProductInfo {
        private String id;
        private String title;
        private String author;
        private String productType;
        private String categoryName;
        private double price;

        public ProductInfo(String id, String title, String author, String productType,
                           String categoryName, double price) {
            this.id = id;
            this.title = title;
            this.author = author;
            this.productType = productType;
            this.categoryName = categoryName;
            this.price = price;
        }

        public String getId() { return id; }
        public String getTitle() { return title; }
        public String getAuthor() { return author; }
        public String getProductType() { return productType; }
        public String getCategoryName() { return categoryName; }
        public double getPrice() { return price; }
    }