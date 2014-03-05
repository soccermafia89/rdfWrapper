///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package ethier.alex.genie;
//
//import ethier.alex.genie.data.*;
//import ethier.alex.genie.exception.OverwriteException;
//import java.util.Collection;
//
///**
//
// @author alex
// */
//public interface GenieInterface {
//    
//    public GenieClass addClass(String classUri, Class myClass) throws OverwriteException;
//    
//    public BlankGenieClass addBlankClass(String classUri);
//    
//    public GenieInstance createInstance(GenieClass genieClass);
//    
//    public BlankGenieInstance createBlankInstance(BlankGenieClass blankGenieClass);
//    
//    public void addStatement(BlankGenieInstance subjectInstance, GenieProperty property, BlankGenieInstance objectInstance);
//    
//    public Collection<GenieStatement> listStatements(BlankGenieInstance subjectInstance, GenieProperty property, BlankGenieInstance objectInstance);
//}
