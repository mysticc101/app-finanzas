# Validación del presupuesto negativo en Spendly

## Objetivo

Comprobar que, al introducir un presupuesto de `-50` y guardarlo, el formulario envía `0` a la operación de guardado `saveBudget`.

La prueba `negativeBudgetSavesAsZero` intercepta esa llamada con Robolectric y comprueba que se realiza una sola vez con importe cero. No verifica una escritura real en la base de datos ni una sincronización con Firebase.

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

**Estado: validación confirmada por Jhon el 4 de noviembre de 2026.**

Jhon (`Jhon-001`) confirmó que siguió todos los pasos sin problemas. Indicó que las instrucciones para la terminal de Windows y la ruta del informe HTML son claras, y que no encontró pasos faltantes ni instrucciones confusas.

| Registro | Resultado |
| --- | --- |
| Ejecutor de la prueba | Jhon (`Jhon-001`) |
| Commit probado, confirmado por Jhon | [5630cd9](https://github.com/mysticc101/app-finanzas/commit/5630cd9851cbcc6155cfb2fcb853a2c9ee31b545) |
| Resultado de Gradle | `BUILD SUCCESSFUL`, según su confirmación y captura de la terminal |
| Prueba del presupuesto negativo | `negativeBudgetSavesAsZero` aprobada |
| Informe aportado | 3 pruebas, 0 fallos, 0 omitidas; 100 % satisfactorio |
| Pasos faltantes o confusos | Ninguno, según Jhon |
| Correcciones tras su revisión | No fueron necesarias correcciones de las instrucciones; se completó este registro con su confirmación y evidencia |

### Evidencia

- [Comentario de Jhon con su confirmación y las capturas](https://github.com/mysticc101/app-finanzas/issues/14#issuecomment-6634877605).
- [Captura de la terminal](https://github.com/user-attachments/assets/edbae74e-b57a-4747-891f-910816835905).
- [Captura del informe de pruebas](https://github.com/user-attachments/assets/de8964a8-7c81-4a2e-ba21-3704c383ee2e).

El resultado aportado por Jhon confirma que la prueba del formulario para el presupuesto de `-50` pasó: la operación de guardado recibe `0`. El alcance es la llamada interceptada por la prueba unitaria, no una escritura real en la base de datos ni una sincronización con Firebase.

Antes de su revisión se habían aclarado la terminal de Windows, la ubicación de la raíz del proyecto, el método que debe aparecer en el informe y cómo obtener una ejecución nueva si se reutilizan resultados. Jhon confirmó que pudo seguir la guía tal como estaba.

La ejecución fue realizada por Jhon. La preparación y actualización de esta guía se basan en la revisión del código, su confirmación y la evidencia aportada; no constituyen una ejecución propia de la prueba.
