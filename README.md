# Pokedex App

## Introducción

La aplicación **Pokedex** es una herramienta interactiva para explorar el mundo de los Pokémon, basada en la API oficial de Pokémon. Permite a los usuarios navegar por una lista de Pokémon, ver sus detalles y capturarlos, guardándolos en su propia colección personal. Además, los usuarios pueden autenticar su acceso de forma segura tanto a través de correo electrónico y contraseña como mediante Google. Los Pokémon capturados se almacenan en Firebase, lo que permite que los usuarios accedan a su colección desde cualquier dispositivo.

---

## Características principales

1. **Autenticación de usuario**:
   - Los usuarios pueden registrarse e iniciar sesión utilizando dos métodos:
     - **Autenticación con correo y contraseña**: Los usuarios pueden crear una cuenta proporcionando su correo electrónico y una contraseña segura.
     - **Autenticación con Google**: Los usuarios pueden iniciar sesión rápidamente con su cuenta de Google.
   
2. **Pokédex**:
   - Los usuarios tienen acceso a una lista completa de Pokémon a través de la API pública de Pokémon.
   - Cada Pokémon en la lista tiene información detallada, como su nombre, tipo, estadísticas y una imagen.
   
3. **Captura de Pokémon**:
   - Los usuarios pueden "capturar" Pokémon, lo que los agrega a su lista personal de Pokémon capturados.
   - Esta lista se guarda en Firebase para que los usuarios puedan verla en futuras sesiones.
   
4. **Lista de Pokémon capturados**:
   - Los usuarios pueden acceder a una sección donde verán todos los Pokémon que han capturado.
   - Los Pokémon capturados se almacenan en la base de datos de Firebase y se sincronizan entre dispositivos.

5. **Ajustes**:
   - Los usuarios pueden acceder a la configuración de la aplicación para gestionar su cuenta y salir de sesión.

---

## Tecnologías utilizadas

1. **Firebase**:
   - **Autenticación de Firebase**: Para manejar la autenticación de usuarios con correo electrónico y contraseña, así como la autenticación con Google.
   - **Firestore**: Para almacenar la lista de Pokémon capturados por cada usuario, proporcionando sincronización en tiempo real y acceso desde cualquier dispositivo.
   
2. **Retrofit**:
   - Se utiliza Retrofit para realizar las solicitudes a la API de Pokémon y obtener los datos sobre los Pokémon, como su nombre, estadísticas e imágenes.

3. **RecyclerView**:
   - Se utiliza RecyclerView para mostrar la lista de Pokémon y los Pokémon capturados en una interfaz de usuario eficiente y desplazable.

4. **Glide**:
   - Glide es utilizado para cargar y mostrar las imágenes de los Pokémon de manera eficiente y sin bloquear el hilo principal de la aplicación.

5. **Kotlin**:
   - Kotlin es el lenguaje de programación principal para el desarrollo de la aplicación, aprovechando sus ventajas como la sintaxis concisa y la integración con Android.

---

## Instrucciones de uso

1. **Clonar el repositorio**:
   - Abre una terminal y clona el repositorio con el siguiente comando:
     ```bash
     git clone https://github.com/tu-usuario/pokedex-app.git
     ```

2. **Abrir el proyecto en Android Studio**:
   - Abre Android Studio y selecciona **Open an existing project**. Luego, navega hasta la carpeta donde clonaste el repositorio y selecciona el proyecto.

3. **Instalar las dependencias**:
   - Android Studio debería solicitar la sincronización de dependencias automáticamente. Si no es así, abre el archivo `build.gradle` (a nivel de proyecto y de módulo) y haz clic en **Sync Now** en la parte superior de Android Studio.

4. **Configurar Firebase**:
   - Asegúrate de haber configurado un proyecto en Firebase y de haber añadido tu archivo `google-services.json` en la carpeta `app/` de tu proyecto.
   - Si no has configurado Firebase, sigue la [guía oficial de Firebase para Android](https://firebase.google.com/docs/android/setup).

5. **Compilar y ejecutar la aplicación**:
   - Conecta tu dispositivo Android o inicia un emulador.
   - Haz clic en **Run** en Android Studio (el ícono del triángulo verde) para compilar y ejecutar la aplicación.

6. **Instalar dependencias adicionales**:
   - Si es necesario, agrega las dependencias de Firebase, Retrofit, Glide y otras bibliotecas en el archivo `build.gradle` de la aplicación. Asegúrate de que todas las versiones de las dependencias sean compatibles con el proyecto.

---

## Conclusiones del desarrollador

El desarrollo de esta aplicación fue una excelente oportunidad para trabajar con tecnologías modernas como Firebase y Retrofit. Algunos de los principales desafíos fueron:

1. **Integración de Firebase**: Configurar la autenticación con Firebase y asegurar que los datos de los usuarios estuvieran correctamente gestionados fue una tarea clave, especialmente la integración de la autenticación con Google.
   
2. **Manejo de la API de Pokémon**: Consumir la API pública de Pokémon y manejar la carga de imágenes de manera eficiente utilizando Glide requirió optimización para asegurar que la experiencia del usuario fuera fluida.

3. **Manejo de estados en la UI**: Como la aplicación depende de datos en tiempo real desde Firebase, se prestó especial atención al manejo de estados, para asegurar que la interfaz se actualice adecuadamente sin bloquear el hilo principal.

En general, este proyecto ha sido una excelente práctica para desarrollar una aplicación interactiva, centrada en el usuario, con almacenamiento en la nube y sincronización en tiempo real.
