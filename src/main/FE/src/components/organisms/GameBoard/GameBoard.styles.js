import styled from 'styled-components';

export const Wrapper = styled.div`
  background-color: #36474f;
  width: 100%;
  min-height: 100vh;
  max-width: 1600px;
  padding: 10px 50px;
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

export const StyledNavigation = styled.div`
  display: flex;
  justify-content: space-evenly;
  background-color: #3ea3f5;
`;

export const StyledGameBoardContainer = styled.div`
  background-color: #fff;
  padding: 50px 0;
  border-left: 7px solid #3ea3f5;
  border-right: 7px solid #3ea3f5;
  border-bottom: 7px solid #3ea3f5;
`;

export const StyledRow = styled.div`
  display: flex;
  width: 100%;
  justify-content: center;
`;

export const HeaderSection = styled.div`
  width: 550px;
  display: flex;
  justify-content: space-between;
  padding: 10px 2px;
`;
