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

        Path fichero = Path.of("data/2025_Accidentalidad.csv");

        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");


        try {

            // Leemos el CSV utilizando Files y un stream
            List<Accidente> accidentes = Files.lines(fichero)
                    .skip(1)
                    .map(linea -> {

                        String[] datos = linea.split(";", -1);

                        if (datos.length != 19){
                            System.out.println("Expediente: [" + datos[0] + "]");
                            System.out.println("COLUMNAS: " + datos.length);
                            System.out.println("SEXO: [" + datos[12] + "]");
                            System.out.println("EDAD: [" + datos[11] + "]");
                        }


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


            accidentes.stream()
                    .map(Accidente::getSexo)
                    .distinct()
                    .forEach(System.out::println);

            /*Map<String, Long> accidentesPorSexo = accidentes.stream()
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
            );*/


        } catch (IOException e) {
            System.out.println("Error al leer el fichero: " + e.getMessage());
        }

    }
}
