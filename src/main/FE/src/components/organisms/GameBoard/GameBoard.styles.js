import styled from 'styled-components';

export const Wrapper = styled.div`
  margin: 25px;
  background-color: #36474f;
  width: 100%;
  height: 100vh;
  min-height: 800px;
  max-width: 1600px;
  padding: 40px 50px;
  border-radius: 25px;
  box-shadow: 0 5px 15px -10px rgba(0, 0, 0, 0.3);
`;

export const StyledList = styled.ul`
  list-style: none;
  margin: 0;
  padding: 0;
`;

export const StyledTitle = styled.h1`
  font-size: ${({ theme }) => theme.fontSize.xl};
  color: ${({ theme }) => theme.colors.darkGrey};
`;

export const StyledRow = styled.div`
  display: flex;
  width: 100%;
  justify-content: center;
  background-color: white;
`;

export const StyledNavigation = styled.div`
  display: flex;
  justify-content: space-evenly;
  background-color: #3ea3f5;
  border-radius: 15px;
`;
