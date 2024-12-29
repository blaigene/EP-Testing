# Informe de Pràctica de Disseny i Proves Unitàries

**Eric Gil, Blai Gene, Oriol Viñes**

## Decisions de Disseny

### Classe `Server`
- **Simulació de base de dades**: Hem utilitzat `HashMaps` per simular la base de dades, facilitant el desenvolupament i les proves sense necessitar una base de dades real.
- **Responsabilitats**: La classe `Server` gestiona la disponibilitat dels vehicles i el registre dels emparellaments i desemparellaments.

### Paquet `services`
- **Ús de fakes**: En les classes dins del paquet `services`, hem utilitzat fakes en els tests per simular el comportament d'aquests components sense implementar la lògica real.
- **Mocks en tests**: Hem utilitzat mocks per simular la classe `JourneyRealizeHandler` en els tests de `UnbondedBTSignal`.

### Sistema de Pagament
- **Estructura del enunciat**: Seguint l'estructura proposada en l'enunciat, hem utilitzat la classe abstracta `Payment` amb `WalletPayment` com a subclasse, i `Wallet` com a classe per gestionar el moneder.

### Paquet `data`
- **Decisions sobre caràcters**: Hem pres decisions específiques sobre els caràcters permesos en els identificadors com `UserAccount`, ajustant-nos als requisits del sistema i millorant la validació de dades.
- **Classes immutables**: Hem definit classes com `GeographicPoint`, `StationID`, `VehicleID`, i `UserAccount` com a immutables per assegurar que no siguin modificades un cop creades.

### Classe `JourneyService`
- **Ús de setters**: Hem optat per utilitzar mètodes setters en comptes de passar totes les dades a través del constructor per permetre una configuració més flexible i escalable de les instàncies.

## Refactoritzacions Realitzades
Enumerem les principals refactoritzacions aplicades durant el desenvolupament:
1. **Encapsulament**: Hem millorat l'encapsulament de les propietats en les classes per protegir l'estat intern i garantir que només es pugui canviar mitjançant mètodes controlats.
2. **Simplificació de mètodes**: Hem refactoritzat mètodes complexos en mètodes més simples per millorar la llegibilitat i el manteniment del codi.
3. **Eliminació de codi duplicat**: Hem identificat i eliminat duplicacions en el codi per centralitzar la lògica i facilitar futures modificacions.

### Code Smells
- **Large Class / God Object**: Algunes classes inicialment tenien massa responsabilitats. A través de la refactorització, hem separat les funcionalitats en classes més petites.
- **Long Method**: Mètodes excessivament llargs han estat refactoritzats per dividir-los en submètodes més petits que realitzen funcions específiques.
- **Feature Envy**: Hem reubicat mètodes que mostraven més interès per les dades d'altres classes a les classes que efectivament posseeixen aquestes dades.

## Principis SOLID
- **Single Responsibility Principle (SRP)**: Cada classe té una única responsabilitat i raó per canviar. Això simplifica la comprensió i manteniment del codi.
- **Open/Closed Principle (OCP)**: El codi està dissenyat per permetre la seva extensió sense modificar el codi existent, facilitant així la introducció de noves funcionalitats.
- **Liskov Substitution Principle (LSP)**: Les subclasses poden substituir les seves classes base sense que afecti la correcció del programa.
- **Interface Segregation Principle (ISP)**: Hem definit múltiples interfícies específiques per a diferents clients en comptes d'una única interfície gran.
- **Dependency Inversion Principle (DIP)**: Els mòduls d'alt nivell no depenen directament dels de baix nivell, sinó que ambdós depenen d'abstraccions.

## Problemes Trobats
- **`JourneyServiceRealizer` i Bluetooth**: Hem experimentat dificultats amb la implementació del Bluetooth, especialment en el maneig de les excepcions i la connectivitat. Això va requerir una revisió profunda del maneig d'errors i la millora de la robustesa del codi.
- **`UnbondedBTSignalTest`**: Durant les proves, vam enfrontar-nos a problemes persistents amb el test de `UnbondedBTSignalTest`, que no vam poder resoldre completament. Això va incloure errors relacionats amb l'ús de Mockito i la configuració de les simulacions, així com dificultats per aconseguir que els tests es comportessin com s'esperava en el context de simulació de Bluetooth.

## Conclusions
Aquest projecte ens ha permès comprendre millor com les decisions de disseny i els patrons de disseny poden influir en la qualitat i mantenibilitat del software. Hem afrontat nombrosos desafiaments, però les solucions implementades han reforçat la nostra capacitat de desenvolupar software robust i mantenible.

**Eric Gil, Blai Gene, Oriol Viñes**
