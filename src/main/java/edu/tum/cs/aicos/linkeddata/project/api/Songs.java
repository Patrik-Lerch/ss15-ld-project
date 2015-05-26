package edu.tum.cs.aicos.linkeddata.project.api;

        import org.openrdf.model.Literal;
        import org.openrdf.query.*;
        import org.openrdf.repository.RepositoryConnection;
        import org.openrdf.repository.RepositoryException;
        import org.openrdf.repository.http.HTTPRepository;

        import java.util.ArrayList;

public class Songs extends ArrayList<Song> {
    public Songs() {

    }

    public Songs(String titel) throws RepositoryException, MalformedQueryException, QueryEvaluationException {


        HTTPRepository httpRepository = new HTTPRepository("http://data.linkedmdb.org/sparql");
        httpRepository.initialize();

        RepositoryConnection repositoryConnection = httpRepository.getConnection();

        TupleQuery tupleQuery = repositoryConnection.prepareTupleQuery(QueryLanguage.SPARQL,


                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                        "PREFIX dc: <http://purl.org/dc/terms/>\n" +
                        "PREFIX movie: <http://data.linkedmdb.org/resource/movie/>\n" +
                        "\n" +
                        "Select ?uri ?titel ?runtime ?publicationDate ?genre\n" +
                        "WHERE {\n" +
                        "?uri movie:runtime ?publicationDate.\n" +
                        "FILTER(?titel >=" + titel + ")\n " +
                        "\n" +
                        "}\n" +
                        "LIMIT 1"


        );


        TupleQueryResult tupleQueryResult = tupleQuery.evaluate();
        while (tupleQueryResult.hasNext()) {
            BindingSet bindingSet = tupleQueryResult.next();

            Song song = new Song();

            Binding songtitelBinding = bindingSet.getBinding("titel");
            Literal songtitelLiteral = (Literal) songtitelBinding.getValue();
            String songtitelString = songtitelLiteral.stringValue();
            song.setSongtitel(songtitelString);

            this.add(song);
        }
        tupleQueryResult.close();

        repositoryConnection.close();

        httpRepository.shutDown();
    }
}