/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.tika.parser.recognition;

/**
 * A model for recognised objects from graphics and texts typically includes
 * human readable label for the object, language of the label, id and confidence score.
 *
 * @since Apache Tika 1.14
 */
public class RecognisedObject {

    /**
     * Label of this object. Usually the name given to this object by humans
     */
    private String label;
    /**
     * Language of label, Example : english
     */
    private String labelLang;
    /**
     * Identifier for this object
     */
    private String id;
    /**
     * Confidence score
     */
    private double confidence;

    public RecognisedObject(String label, String labelLang, String id, double confidence) {
        this.label = label;
        this.labelLang = labelLang;
        this.id = id;
        this.confidence = confidence;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabelLang() {
        return labelLang;
    }

    public void setLabelLang(String labelLang) {
        this.labelLang = labelLang;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    @Override
    public String toString() {
        return "RecognisedObject{" +
                "label='" + label + "\' (" + labelLang + ')' +
                ", id='" + id + '\'' +
                ", confidence=" + confidence +
                '}';
    }
}
