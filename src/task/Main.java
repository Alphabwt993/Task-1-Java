package task;


import task.service.UserService;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserService();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("Приложение для управления пользователями запущено.");

        while (running) {
            System.out.println("\n=== Консольное меню ===");
            System.out.println("1. Добавить пользователя вручную");
            System.out.println("2. Загрузить пользователей из файла (CSV)");
            System.out.println("3. Показать всех пользователей");
            System.out.println("4. Обработать данные через Stream API");
            System.out.println("5. Выход");
            System.out.print("Выберите пункт меню: ");

            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1" -> {
                        // 1. Пошаговый ввод ID с мгновенной валидацией
                        int validatedId;
                        while (true) {
                            System.out.print("Введите ID пользователя: ");
                            String idStr = scanner.nextLine();
                            try {
                                validatedId = userService.validateAndParseId(idStr);
                                break; // Если ошибок нет, выходим из цикла ввода ID
                            } catch (IllegalArgumentException e) {
                                System.err.println("Ошибка валидации: " + e.getMessage());
                            }
                        }

                        // 2. Пошаговый ввод Имени с мгновенной валидацией
                        String validatedName;
                        while (true) {
                            System.out.print("Введите имя пользователя: ");
                            validatedName = scanner.nextLine();
                            try {
                                userService.validateName(validatedName);
                                break; // Если ошибок нет, выходим из цикла ввода имени
                            } catch (IllegalArgumentException e) {
                                System.err.println("Ошибка валидации: " + e.getMessage());
                            }
                        }

                        // 3. Пошаговый ввод Email с мгновенной валидацией
                        String validatedEmail;
                        while (true) {
                            System.out.print("Введите email: ");
                            validatedEmail = scanner.nextLine();
                            try {
                                userService.validateEmail(validatedEmail);
                                break; // Если ошибок нет, выходим из цикла ввода email
                            } catch (IllegalArgumentException e) {
                                System.err.println("Ошибка валидации: " + e.getMessage());
                            }
                        }

                        // Сохраняем уже гарантированно валидного пользователя
                        userService.addValidatedUser(validatedId, validatedName, validatedEmail);
                        System.out.println("Пользователь успешно добавлен!");
                    }
                    case "2" -> {
                        System.out.print("Введите путь к CSV файлу (например, users.txt): ");
                        String filePath = scanner.nextLine();
                        userService.loadUsersFromFile(filePath);
                        System.out.println("Данные из файла успешно загружены!");
                    }
                    case "3" -> {
                        System.out.println("\nСписок всех пользователей:");
                        if (userService.getAllUsers().isEmpty()) {
                            System.out.println("Список пуст.");
                        } else {
                            userService.getAllUsers().forEach(System.out::println);
                        }
                    }
                    case "4" -> userService.processAndDisplayData();
                    case "5" -> {
                        running = false;
                        System.out.println("Программа завершена. До свидания!");
                    }
                    default -> System.out.println("Неверный пункт меню. Попробуйте снова.");
                }
            } catch (IOException e) {
                System.err.println("Ошибка при работе с файлом: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Произошла непредвиденная ошибка: " + e.getMessage());
            }
        }
        scanner.close();
    }
}
