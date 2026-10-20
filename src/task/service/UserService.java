package task.service;


import task.model.User;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class UserService {
    private final List<User> users = new ArrayList<>();

    // Старый метод для чтения из файла (вызывает цепочку валидаций)
    public void addUser(String idStr, String name, String email) throws IllegalArgumentException {
        int id = validateAndParseId(idStr);
        validateName(name);
        validateEmail(email);
        addValidatedUser(id, name, email);
    }

    // Новый метод для добавления уже проверенного в консоли пользователя
    public void addValidatedUser(int id, String name, String email) {
        users.add(new User(id, name, email));
    }

    // Чтение пользователей из CSV файла с try-with-resources
    public void loadUsersFromFile(String filePath) throws IOException, IllegalArgumentException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length != 3) {
                    throw new IllegalArgumentException("Ошибка в строке " + lineNumber + ": должно быть 3 поля через запятую.");
                }

                addUser(parts[0].trim(), parts[1].trim(), parts[2].trim());
            }
        }
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    public void processAndDisplayData() {
        if (users.isEmpty()) {
            System.out.println("Коллекция пуста. Сначала добавьте данные.");
            return;
        }

        System.out.println("\n--- Результат обработки Stream API ---");
        users.stream()
                .filter(user -> user.getName().length() > 3)
                .map(user -> new User(user.getId(), user.getName(), user.getEmail().toUpperCase()))
                .sorted(Comparator.comparingInt(User::getId))
                .forEach(System.out::println);
        System.out.println("------------------------------------");
    }

    // --- Блок валидации данных (теперь методы public для мгновенной проверки) ---
    public int validateAndParseId(String idStr) {
        try {
            int id = Integer.parseInt(idStr);
            if (id <= 0) {
                throw new IllegalArgumentException("ID пользователя должен быть положительным числом. Введите ID:");
            }
            boolean idExists = users.stream().anyMatch(u -> u.getId() == id);
            if (idExists) {
                throw new IllegalArgumentException("Пользователь с ID " + id + " уже существует.");
            }
            return id;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ID пользователя должен быть корректным целым числом.");
        }
    }

    public void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя пользователя не может быть пустым.");
        }
    }

    public void validateEmail(String email) {
        if (email == null || !email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("Некорректный формат email (должен содержать '@' и '.').");
        }
    }
}
