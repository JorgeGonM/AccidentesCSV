import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {

        System.out.println("Accidentes de Madrid 2025");

        Path fichero = Path.of("../../../data/2025_Accidentalidad.csv");

        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");


        try {

            // Leemos el CSV utilizando Files y un stream
            List<Accidente> accidentes = Files.lines(fichero)
                    .skip(1)
                    .map(linea -> {

                        String[] datos = separarCSV(linea);

                        return new Accidente(
                                //Cada indice es la columna

                                datos[0],   // num_expediente
                                LocalDate.parse(datos[1], formatoFecha),
                                datos[7],   // tipo_accidente
                                datos[12],  // sexo
                                datos[17],  // positiva_alcohol
                                datos[18]   // positiva_droga
                        );
                    })
                    .collect(Collectors.toList());


            //1.- Accidentes en los que están implicados alcohol o drogas

            System.out.println();
            System.out.println("Accidentes en los que están implicados alcohol o drogas:");

            accidentes.stream()
                    .filter(accidente ->
                            "S".equals(accidente.getPositivaAlcohol())
                                    || "1".equals(accidente.getPositivaDroga())
                    )
                    .map(Accidente::getNumExpediente)
                    .distinct();
                    //.forEach(System.out::println);


            //2.- Número de accidentes agrupados por sexo
            System.out.println();
            System.out.println("2. Número de accidentes agrupados por sexo:");

            Map<String, Long> accidentesPorSexo = accidentes.stream()
                    .filter(accidente -> accidente.getSexo() != null)
                    .collect(Collectors.groupingBy(
                            Accidente::getSexo,
                            Collectors.mapping(
                                    Accidente::getNumExpediente,
                                    Collectors.collectingAndThen(
                                            Collectors.toSet(),
                                            conjunto -> (long) conjunto.size()
                                    )
                            )
                    ));

            accidentesPorSexo.forEach((sexo, cantidad) ->
                    System.out.println(
                            sexo + ": " + cantidad + " accidentes"
                    )
            );


            //3.- Número de accidentes agrupados por meses
            System.out.println();
            System.out.println("3. Número de accidentes agrupados por meses:");

            Map<Integer, Long> accidentesPorMes = accidentes.stream()
                    .collect(Collectors.groupingBy(
                            accidente -> accidente.getFecha().getMonthValue(),
                            Collectors.mapping(
                                    Accidente::getNumExpediente,
                                    Collectors.collectingAndThen(
                                            Collectors.toSet(),
                                            conjunto -> (long) conjunto.size()
                                    )
                            )
                    ));

                accidentesPorMes.forEach((mes, cantidad) ->
                    System.out.println(
                            "Mes " + mes + ": " + cantidad + " accidentes"
                    )
                );

                //4.- Accidentes donde haya habido atropellos a personas
                System.out.println();
                System.out.println("4. Accidentes donde haya habido atropellos a personas:");

                accidentes.stream()
                    .filter(accidente -> accidente.getTipoAccidente() != null)
                    .filter(accidente -> accidente.getTipoAccidente().contains("Atropello a persona"))
                    .map(Accidente::getNumExpediente)
                    .distinct()
                    .forEach(System.out::println);
                    

                //Numero total de accidentes a personas 
                long totalAtropellos = accidentes.stream()
                    .filter(accidente -> accidente.getTipoAccidente() != null)
                    .filter(accidente -> accidente.getTipoAccidente().contains("Atropello a persona"))
                    .map(Accidente::getNumExpediente)
                    .distinct()
                    .count();
                    System.out.println("Total de accidentes a personas: " + totalAtropellos);





        } catch (IOException e) {
            System.out.println("Error al leer el fichero: " + e.getMessage());
        }


    }

//Método para separar una línea de CSV en campos, considerando comillas y punto y coma como separador   
private static String[] separarCSV(String linea) {

    List<String> campos = new java.util.ArrayList<>();
    StringBuilder campo = new StringBuilder();
    boolean dentroComillas = false;

    for (int i = 0; i < linea.length(); i++) {

        char caracter = linea.charAt(i);

        if (caracter == '"') {
            dentroComillas = !dentroComillas;
        } else if (caracter == ';' && !dentroComillas) {
            campos.add(campo.toString());
            campo.setLength(0);
        } else {
            campo.append(caracter);
        }
    }

    campos.add(campo.toString());

    return campos.toArray(new String[0]);
}


}
