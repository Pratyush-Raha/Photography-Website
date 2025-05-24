import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

class Photo {
    String title;
    String category;
    int rating;
    String photographerName;
    String uploadDate;
    String description;

    Photo(String title, String category, String photographerName, String uploadDate, String description) {
        this.title = title;
        this.category = category;
        this.photographerName = photographerName;
        this.uploadDate = uploadDate;
        this.description = description;
        this.rating = 0;
    }

    void display() {
        System.out.println("Title: " + title + 
                           " | Category: " + category + 
                           " | Photographer: " + photographerName + 
                           " | Upload Date: " + uploadDate +
                           " | Description: " + description +
                           " | Rating: " + rating + "/5");
    }

    public String toFileFormat() {
        return title + ";" + category + ";" + photographerName + ";" + uploadDate + ";" + description + ";" + rating;
    }
}

class Gallery {
    ArrayList<Photo> photos = new ArrayList<>();
    String storageFile = "gallery_data.txt";

    void loadPhotos() {
        try (BufferedReader reader = new BufferedReader(new FileReader(storageFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length == 6) {
                    Photo photo = new Photo(data[0], data[1], data[2], data[3], data[4]);
                    photo.rating = Integer.parseInt(data[5]);
                    photos.add(photo);
                }
            }
        } catch (IOException e) {
            System.out.println("No previous data found. Starting fresh.");
        }
    }

    void savePhotos() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(storageFile))) {
            for (Photo photo : photos) {
                writer.write(photo.toFileFormat());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving photo data: " + e.getMessage());
        }
    }

    void addPhoto(String title, String category, String photographerName, String uploadDate, String description) {
        for (Photo photo : photos) {
            if (photo.title.equalsIgnoreCase(title)) {
                System.out.println("Photo with this title already exists.");
                return;
            }
        }

        // Date Format Validation
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dateFormat.setLenient(false);
        try {
            Date date = dateFormat.parse(uploadDate);
            uploadDate = dateFormat.format(date);
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please use dd-MM-yyyy.");
            return;
        }

        photos.add(new Photo(title, category, photographerName, uploadDate, description));
        savePhotos();
        System.out.println("Photo added successfully.");
    }

    void deletePhoto(String title) {
        Iterator<Photo> iterator = photos.iterator();
        while (iterator.hasNext()) {
            Photo photo = iterator.next();
            if (photo.title.equalsIgnoreCase(title)) {
                iterator.remove();
                savePhotos();
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
                savePhotos();
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

        gallery.loadPhotos();

        while (true) {
            System.out.println("\n--- Photography Gallery ---");
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

            int choice = Integer.parseInt(scanner.nextLine());

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
                    int rating = Integer.parseInt(scanner.nextLine());
                    gallery.ratePhoto(rateTitle, rating);
                    break;
                case 5:
                    System.out.print("Enter title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter category: ");
                    String category = scanner.nextLine();
                    System.out.print("Enter photographer name: ");
                    String photographerName = scanner.nextLine();
                    System.out.print("Enter upload date (dd-MM-yyyy): ");
                    String uploadDate = scanner.nextLine();
                    System.out.print("Enter description: ");
                    String description = scanner.nextLine();
                    gallery.addPhoto(title, category, photographerName, uploadDate, description);
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
                    System.out.println("Invalid choice.");
            }
        }
    }
}
