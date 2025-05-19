<h1>Мобильные шашки</h1>

<h2>📌 Описание</h2>
Этот проект представляет собой Android-приложение для игры в шашки, написанное на языке Kotlin. Он предоставлен в виде нескомпилированного исходного кода, что позволяет вам настроить, расширить и собрать приложение под нужные устройства и требования.
Важно: Проект не включает готовый APK-файл. Для запуска необходимо собрать его вручную с помощью Android Studio и соответствующих инструментов.

<h2>📁 Структура проекта</h2>
mobile-chekers/
├── app/                # Основной модуль приложения (src, res, AndroidManifest.xml)
├── gradle/             # Конфигурации Gradle
├── build.gradle (Project)  
├── build.gradle (Module)  
└── README.md           # Текущий файл
<h2>🚀 Установка и сборка</h2>

- Установите Android Studio
 
- Скачайте и установите Android Studio с официального сайта.
 
- Убедитесь, что установлены Android SDK, Android Virtual Device (AVD) и Java Development Kit (JDK).

- Импорт проекта в Android Studio.

- Откройте Android Studio.

- Выберите "Open an existing Android project".

- Найдите папку mobile-chekers и откройте её.

- Android Studio автоматически настроит проект, если все зависимости и конфигурации корректны.

<h2>Настройка окружения</h2>

- Убедитесь, что в build.gradle (Module) указана совместимая версия SDK и Gradle.
  
- Если возникают ошибки, проверьте:
  
  - Установлен ли Android SDK и JDK.
    
  - Обновлен ли Gradle (в build.gradle (Project)).
    
  - Соответствует ли версия SDK, указанная в AndroidManifest.xml, установленной на вашем компьютере.
    
<h2>Сборка и запуск</h2>

- Нажмите "Build" → "Make Project" для сборки. Или используйте "Run" → "Run 'app'".

- Если вы хотите создать APK, нажмите "Build" → "Build Bundle(s) or APK(s)" → "Build APK(s)".
  
- APK будет сохранён в папку app/build/outputs/apk/debug/ (или release/ в зависимости от конфигурации).
  
<h2>🛠️ Зависимости</h2>

- Android SDK (версия, указанная в build.gradle).
  
- Java JDK (версия, указанная в build.gradle).
  
- Gradle (версия, указанная в build.gradle (Project)).
  
- Android Studio (рекомендуется использовать последнюю стабильную версию).

Happy coding! 🚀
