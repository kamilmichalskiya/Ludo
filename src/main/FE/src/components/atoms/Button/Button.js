import styled from 'styled-components';

export const Button = styled.button`
  margin: 15px 0;
  padding: 7px 20px;
  font-size: ${({ theme }) => theme.fontSize.s};
  background-color: ${({ theme }) => theme.colors.lightPurple};
  border-radius: 20px;
  border: none;
  font-weight: bold;
  color: ${({ theme }) => theme.colors.darkGrey};
`;

export const PrimaryButton = styled.button`
  margin: 15px 0px;
  padding: 12px 20px;
  font-size: ${({ theme }) => theme.fontSize.s};
  background-color: ${({ theme }) => theme.colors.white};
  border-radius: 5px;
  border: none;
  font-weight: bold;
  color: ${({ theme }) => theme.colors.black};
  position: relative;
  left: 0;
  hover: {
    background-color: yellow;
  };
  cursor: pointer;
`;
