import java.util.*;

class Photo {
    String title;
    String category;
    int rating;

    Photo(String title, String category) {
        this.title = title;
        this.category = category;
        this.rating = 0;
    }

    void display() {
        System.out.println("Title: " + title + " | Category: " + category + " | Rating: " + rating + "/5");
    }
}

class Gallery {
    ArrayList<Photo> photos = new ArrayList<>();

    void addPhoto(String title, String category) {
        for (Photo photo : photos) {
            if (photo.title.equalsIgnoreCase(title)) {
                System.out.println("Photo with this title already exists.");
                return;
            }
        }
        photos.add(new Photo(title, category));
        System.out.println("Photo added successfully.");
    }

    void deletePhoto(String title) {
        Iterator<Photo> iterator = photos.iterator();
        while (iterator.hasNext()) {
            Photo photo = iterator.next();
            if (photo.title.equalsIgnoreCase(title)) {
                iterator.remove();
                System.out.println("Photo deleted successfully.");
                return;
            }
        }
        System.out.println("Photo not found.");
    }

    void showGallery() {
        if (photos.isEmpty()) {
            System.out.println("Gallery is empty.");
            return;
        }
        System.out.println("Photo Gallery:");
        for (Photo photo : photos) {
            photo.display();
        }
    }

    void searchPhoto(String title) {
        for (Photo photo : photos) {
            if (photo.title.equalsIgnoreCase(title)) {
                photo.display();
                return;
            }
        }
        System.out.println("Photo not found.");
    }

    void filterByCategory(String category) {
        boolean found = false;
        for (Photo photo : photos) {
            if (photo.category.equalsIgnoreCase(category)) {
                photo.display();
                found = true;
            }
        }
        if (!found) {
            System.out.println("No photos found in this category.");
        }
    }

    void ratePhoto(String title, int rating) {
        if (rating < 1 || rating > 5) {
            System.out.println("Invalid rating. Please enter a number between 1 and 5.");
            return;
        }

        for (Photo photo : photos) {
            if (photo.title.equalsIgnoreCase(title)) {
                photo.rating = rating;
                System.out.println("Photo rated successfully.");
                return;
            }
        }
        System.out.println("Photo not found.");
    }
}

public class Photography {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Gallery gallery = new Gallery();

        while (true) {
            System.out.println("\n--- Photography Website ---");
            System.out.println("1. View Gallery");
            System.out.println("2. Search Photo");
            System.out.println("3. Filter by Category");
            System.out.println("4. Rate Photo");
            System.out.println("5. Upload Photo (Admin)");
            System.out.println("6. Delete Photo (Admin)");
            System.out.println("7. Simulate Payment");
            System.out.println("8. Share on Social Media");
            System.out.println("9. Exit");
            System.out.print("Enter choice: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    gallery.showGallery();
                    break;
                case 2:
                    System.out.print("Enter photo title to search: ");
                    gallery.searchPhoto(scanner.nextLine());
                    break;
                case 3:
                    System.out.print("Enter category: ");
                    gallery.filterByCategory(scanner.nextLine());
                    break;
                case 4:
                    System.out.print("Enter photo title to rate: ");
                    String rateTitle = scanner.nextLine();
                    System.out.print("Enter rating (1-5): ");
                    int rating;
                    try {
                        rating = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid rating input.");
                        break;
                    }
                    gallery.ratePhoto(rateTitle, rating);
                    break;
                case 5:
                    System.out.print("Enter title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter category: ");
                    String category = scanner.nextLine();
                    gallery.addPhoto(title, category);
                    break;
                case 6:
                    System.out.print("Enter title to delete: ");
                    gallery.deletePhoto(scanner.nextLine());
                    break;
                case 7:
                    System.out.println("Payment successful! (simulated)");
                    break;
                case 8:
                    System.out.println("Shared to Instagram (simulated)");
                    break;
                case 9:
                    System.out.println("Exiting... Thank you!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please select from 1 to 9.");
            }
        }
    }
}
