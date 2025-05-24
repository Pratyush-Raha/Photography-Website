import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.text.*;
import java.util.*;

// ==================== Parent Class ====================
class Photo {
    String title;
    String category;
    int rating;
    String photographerName;
    String uploadDate;
    String description;

    public Photo(String title, String category, String photographerName, String uploadDate, String description) {
        this.title = title;
        this.category = category;
        this.photographerName = photographerName;
        this.uploadDate = uploadDate;
        this.description = description;
        this.rating = 0;
    }

    public String toString() {
        return "Title: " + title +
               " Category: " + category +
               " Photographer: " + photographerName +
               " Upload Date: " + uploadDate +
               " Description: " + description +
               " Rating: " + rating + "/5";
    }

    public String toFileFormat() {
        return title + ";" + category + ";" + photographerName + ";" + uploadDate + ";" + description + ";" + rating;
    }
}

// ==================== Child Class ====================
class Gallery {
    ArrayList<Photo> photos = new ArrayList<>();
    String storageFile = "gallery_data.txt";

    public void loadPhotos() {
        photos.clear();
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
            System.out.println("No previous data found.");
        }
    }

    public void savePhotos() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(storageFile))) {
            for (Photo photo : photos) {
                writer.write(photo.toFileFormat());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving photo data.");
        }
    }

    public void addPhoto(Photo photo) {
        for (Photo p : photos) {
            if (p.title.equalsIgnoreCase(photo.title)) {
                JOptionPane.showMessageDialog(null, "Photo with this title already exists.");
                return;
            }
        }
        photos.add(photo);
        savePhotos();
    }

    public void deletePhoto(String title) {
        Iterator<Photo> iterator = photos.iterator();
        while (iterator.hasNext()) {
            Photo p = iterator.next();
            if (p.title.equalsIgnoreCase(title)) {
                iterator.remove();
                savePhotos();
                JOptionPane.showMessageDialog(null, "Photo deleted.");
                return;
            }
        }
        JOptionPane.showMessageDialog(null, "Photo not found.");
    }

    public Photo searchPhoto(String title) {
        for (Photo p : photos) {
            if (p.title.equalsIgnoreCase(title)) {
                return p;
            }
        }
        return null;
    }

    public ArrayList<Photo> filterByCategory(String category) {
        ArrayList<Photo> result = new ArrayList<>();
        for (Photo p : photos) {
            if (p.category.equalsIgnoreCase(category)) {
                result.add(p);
            }
        }
        return result;
    }

    public void ratePhoto(String title, int rating) {
        for (Photo p : photos) {
            if (p.title.equalsIgnoreCase(title)) {
                p.rating = rating;
                savePhotos();
                return;
            }
        }
    }
}

// ==================== Main Class ====================
public class Photography extends JFrame {
    Gallery gallery = new Gallery();
    JTextArea outputArea;

    public Photography() {
        gallery.loadPhotos();

        setTitle("Photography Gallery");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 3));
        add(buttonPanel, BorderLayout.SOUTH);

        JButton viewBtn = new JButton("View Gallery");
        JButton searchBtn = new JButton("Search Photo");
        JButton filterBtn = new JButton("Filter by Category");
        JButton rateBtn = new JButton("Rate Photo");
        JButton uploadBtn = new JButton("Upload Photo");
        JButton deleteBtn = new JButton("Delete Photo");
        JButton payBtn = new JButton("Simulate Payment");
        JButton shareBtn = new JButton("Share on Social Media");
        JButton exitBtn = new JButton("Exit");

        buttonPanel.add(viewBtn);
        buttonPanel.add(searchBtn);
        buttonPanel.add(filterBtn);
        buttonPanel.add(rateBtn);
        buttonPanel.add(uploadBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(payBtn);
        buttonPanel.add(shareBtn);
        buttonPanel.add(exitBtn);

        viewBtn.addActionListener(e -> displayPhotos(gallery.photos));
        searchBtn.addActionListener(e -> {
            String title = JOptionPane.showInputDialog("Enter title:");
            Photo p = gallery.searchPhoto(title);
            outputArea.setText(p == null ? "Not found." : p.toString());
        });
        filterBtn.addActionListener(e -> {
            String category = JOptionPane.showInputDialog("Enter category:");
            displayPhotos(gallery.filterByCategory(category));
        });
        rateBtn.addActionListener(e -> {
            String title = JOptionPane.showInputDialog("Enter title:");
            String rateStr = JOptionPane.showInputDialog("Enter rating (1-5):");
            int rate = Integer.parseInt(rateStr);
            gallery.ratePhoto(title, rate);
            outputArea.setText("Rated successfully.");
        });
        uploadBtn.addActionListener(e -> {
            JTextField title = new JTextField();
            JTextField category = new JTextField();
            JTextField photographer = new JTextField();
            JTextField date = new JTextField();
            JTextField desc = new JTextField();

            Object[] fields = {
                "Title:", title,
                "Category:", category,
                "Photographer:", photographer,
                "Upload Date (dd-MM-yyyy):", date,
                "Description:", desc
            };

            int result = JOptionPane.showConfirmDialog(null, fields, "Upload Photo", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                    df.setLenient(false);
                    df.parse(date.getText());

                    gallery.addPhoto(new Photo(title.getText(), category.getText(), photographer.getText(), date.getText(), desc.getText()));
                    outputArea.setText("Photo added successfully.");
                } catch (ParseException ex) {
                    outputArea.setText("Invalid date format.");
                }
            }
        });
        deleteBtn.addActionListener(e -> {
            String title = JOptionPane.showInputDialog("Enter title to delete:");
            gallery.deletePhoto(title);
        });
        payBtn.addActionListener(e -> outputArea.setText("Payment successful (simulated)."));
        shareBtn.addActionListener(e -> outputArea.setText("Shared on Instagram (simulated)."));
        exitBtn.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    void displayPhotos(ArrayList<Photo> list) {
        if (list.isEmpty()) {
            outputArea.setText("No photos found.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Photo p : list) {
            sb.append(p.toString()).append("\n\n");
        }
        outputArea.setText(sb.toString());
    }

    public static void main(String[] args) {
        new Photography();
    }
}