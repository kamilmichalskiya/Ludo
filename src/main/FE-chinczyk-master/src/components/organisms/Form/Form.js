import React from 'react';
import { StyledTitle, Wrapper } from '../GamesList/GamesList.styles';
import FormField from 'components/molecules/FormField/FormField';
import { Button } from 'components/atoms/Button/Button';

const Form = ({ formValues, handleInputChange }) => {
  return (
    <>
      <FormField label="Name" id="name" name="name" value={formValues.name} onChange={handleInputChange} />
    </>
  );
};

export default Form;
