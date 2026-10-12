# Inventario de configuración de Spendly

Responsable del código, configuración, pruebas y registros técnicos: @mysticc101. Base inicial: 2394201. Configuración de código validada: [87b1941](https://github.com/mysticc101/app-finanzas/commit/87b19419b31762d8541a3ae4ae2aca5a553aabe2), rama `final`. Los documentos técnicos se identifican mediante el commit que contiene cada archivo (`git log -- ruta`).

| ID | Elemento y ubicación | Versión o identificación | Relaciones y control |
|---|---|---|---|
| EC-01 | Código: `app/src/main/java/` | Commit 87b1941 | UI → contrato → adaptador → servicios; Git y RFC 001–004. |
| EC-02 | Recursos y manifiesto: `app/src/main/res/`, `app/src/main/AndroidManifest.xml` | Árbol del mismo commit; recursos conservados | Referencias R utilizadas por interfaz y estrategias; Git y pruebas de equivalencia. |
| EC-03 | Build: `app/build.gradle.kts`, `build.gradle.kts`, `settings.gradle.kts`, `gradle/libs.versions.toml`, wrapper | Gradle 8.13; AGP 8.12.3; Kotlin 2.3.20; Java/JVM 17 | Declara plugins y dependencias; se controla junto al código. |
| EC-04 | Persistencia: `data/local/room/AppRoomDatabase.kt`, entidades, DAO y migraciones | Esquema 9; Room 2.6.1; `finanzas_local.db` | Git controla definición/migraciones. `exportSchema=false`: no existe exportación JSON del esquema. La base con datos de usuario permanece en el dispositivo. |
| EC-05 | Pruebas: `app/src/test/`, `app/src/androidTest/` | Commit 87b1941; JUnit 4.13.2; Robolectric 4.16.1 | Verifican EC-01/02/04; 59 locales y 23 instrumentadas ejecutadas. |
| EC-06 | Evidencias: `docs/maintenance/2026-10-11/evidencias/` | Timestamp de ejecución y SHA-256 | XML, logs depurados y procedencia; relación con fuentes y RFC. |
| EC-07 | Gestión: RFC de este directorio e issues #9–#13 | Identificador RFC, número de issue y commit documental | Git conserva registros; GitHub conserva asignación, etiquetas, comentarios y cierre. |
| EC-08 | Artefacto debug: `app/build/outputs/apk/debug/Spendly-debug.apk` | versionName 1.0.0, versionCode 7 | Derivado de fuente/build; build/ está excluido de Git. No es una release firmada publicada. |
| EC-09 | Configuración de entorno: `local.properties`, `google-services.json` y claves de firma | Configuración local excluida | Se suministra por canales privados; no se publica ni se reemplaza con valores reales en ejemplos. |

Los números del catálogo no se consideran automáticamente dependencias efectivas: algunos módulos declaran versiones directas en sus archivos Gradle. La aplicación mantiene compile SDK 36, target 35 y min 26. Este paquete añade Robolectric solo a pruebas; no actualiza dependencias productivas.

## Mecanismos de control

Identificación mediante ruta, ID del elemento y hash de commit. Las RFC describen impacto, riesgo y criterio de aceptación. La selección explícita de archivos evita incorporar configuración privada y artefactos generados. Antes de publicar se revisan el diff, las pruebas disponibles y su correspondencia con las fuentes. La consulta al remoto confirma el hash publicado y los issues registran el cierre.

La configuración de referencia reúne el código, los archivos de compilación y las pruebas que se validaron juntos. El identificador del commit permite recuperar ese conjunto. Los valores versionName/versionCode identifican la versión declarada de Android, mientras que una etiqueta de Git marca una revisión del repositorio. Este mantenimiento se identifica mediante commits y no tiene una release asociada.

## Distribución

El repositorio distribuye el código, su configuración de compilación, las pruebas y la documentación técnica. Las bases de datos de usuarios, las credenciales, las claves de firma y los archivos de configuración privada permanecen fuera de Git. Las evidencias publicadas omiten datos personales del equipo utilizado para las pruebas.
