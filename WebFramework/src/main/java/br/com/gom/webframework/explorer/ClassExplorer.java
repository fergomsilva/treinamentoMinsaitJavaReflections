package br.com.gom.webframework.explorer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import br.com.gom.webframework.util.WebFrameworkLogger;

public class ClassExplorer{

    private ClassExplorer(){
        super();
    }
    
    public static List<String> retrieveAllClasses(final Class<?> sourceClass){
        return packagExplorer( sourceClass.getPackageName() );
    }

    private static List<String> packagExplorer(final String packageName){
        final List<String> classNames = new ArrayList<>();
        try{
            // dado a pasta onde tenho os pacotes do projeto com getResourceAsStream, 
            // defino raiz como package
            final InputStream stream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream( packageName.replaceAll( "\\.", "/" ) );
            final BufferedReader reader = new BufferedReader( new java.io.InputStreamReader( stream ) );
            String line;
            while( ( line = reader.readLine() ) != null ){
                if( line.endsWith( ".class" ) ){
                    final String className = packageName + "." 
                        + line.substring( 0, line.lastIndexOf( ".class" ) );
                    classNames.add( className );
                }else{
                    // recursividade
                    classNames.addAll( packagExplorer( packageName + "." + line ) );
                }
            }
        }catch( Exception e ){
            WebFrameworkLogger.error( "Class Explorer", e, e.getLocalizedMessage() );
        }
        return classNames;
    }

}