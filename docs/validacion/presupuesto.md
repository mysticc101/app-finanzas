# Validación del presupuesto negativo en Spendly

## Objetivo

Comprobar que, al introducir un presupuesto de `-50` y guardarlo, el formulario envía `0` a la operación de guardado `saveBudget`.

La prueba `negativeBudgetSavesAsZero` intercepta esa llamada con Robolectric y comprueba que se realiza una sola vez con importe cero. No verifica una escritura real en Firebase.

## Preparación

1. Instalar Android Studio y Git.
2. En el SDK Manager de Android Studio, instalar **Android SDK Platform 36**.
3. Descargar el proyecto y seleccionar la rama **qa/validacion-de-movimientos**. Para una descarga nueva con Git:

   ```powershell
   git clone --branch qa/validacion-de-movimientos https://github.com/mysticc101/app-finanzas.git
   cd app-finanzas
   ```

   Si ya se dispone del proyecto, actualizar esa rama sin descartar cambios locales.
4. Abrir la carpeta principal del proyecto en Android Studio.
5. Incorporar la configuración local de Firebase suministrada por privado, siguiendo las indicaciones del equipo. No incluir esa configuración en el commit de documentación.
6. Esperar a que termine la sincronización de Gradle y resolver los errores de configuración que aparezcan.

## Ubicación de la prueba

`app/src/test/java/com/example/finanzas/ui/BudgetFormRegressionTest.java`

Comprobar que el archivo contiene el método `negativeBudgetSavesAsZero`. La clase también incluye otras pruebas de validación del formulario; ejecutar la clase completa las incluye.

## Cómo ejecutarla en Windows

Abrir la terminal de Android Studio en la carpeta principal del proyecto, donde está `gradlew.bat`, y ejecutar:

```powershell
.\gradlew.bat :app:testDebugUnitTest --tests "com.example.finanzas.ui.BudgetFormRegressionTest"
```

Esperar a que termine el comando. Es una prueba unitaria con Robolectric; no requiere iniciar un emulador ni conectar un teléfono.

## Cómo revisar el resultado

1. Comprobar que Gradle termine correctamente con `BUILD SUCCESSFUL`.
2. Abrir en el navegador el informe local:

   `app/build/reports/tests/testDebugUnitTest/index.html`

3. Entrar en `com.example.finanzas.ui.BudgetFormRegressionTest` y verificar que `negativeBudgetSavesAsZero` figure como aprobada, no omitida, y que el informe no muestre fallos.
4. Confirmar que el informe corresponde a la versión y ejecución revisadas. Si Gradle muestra `UP-TO-DATE` o `FROM-CACHE` y se necesita una ejecución nueva como evidencia, repetir el mismo comando añadiendo `--rerun-tasks`.
5. Guardar la salida de la terminal y una captura o copia del informe con el resultado de la prueba, junto con el commit probado.

Si falla la preparación o la compilación, registrar el error: ese bloqueo no demuestra el resultado de la validación de `-50` a `0`.

## Referencia del código de Jhon

- Autor: `Jhon-001`.
- Commit: [test: verificar el guardado de presupuestos negativos](https://github.com/mysticc101/app-finanzas/commit/5630cd9851cbcc6155cfb2fcb853a2c9ee31b545).
- La referencia identifica el cambio que incorpora la prueba; no constituye evidencia de su ejecución.

## Revisión de la guía con Jhon

**Estado: pendiente de confirmación y evidencia de Jhon.**

Solicitar a Jhon que siga esta guía al ejecutar la prueba y confirme:
- Si faltó algún paso o alguna instrucción resultó confusa.
- El commit exacto que probó.
- El resultado de Gradle y de `negativeBudgetSavesAsZero`.
- La evidencia de la terminal y del informe, sin incluir configuración privada.

| Registro | Estado |
| --- | --- |
| Resultado confirmado por Jhon | Pendiente |
| Evidencia de ejecución | Pendiente |
| Pasos faltantes o confusos indicados por Jhon | Pendiente |
| Correcciones realizadas tras su revisión | Pendiente |

En esta versión se aclararon la terminal de Windows, la ubicación de la raíz del proyecto, el método que debe aparecer en el informe y cómo obtener una ejecución nueva si se reutilizan resultados. Estas aclaraciones proceden de la revisión del código y de la guía; todavía no de comentarios de Jhon.

La preparación y revisión de este documento no implica haber ejecutado la prueba. Completar el resultado y las correcciones únicamente cuando Jhon aporte su confirmación y evidencia.
