import styled from 'styled-components';

export const Wrapper = styled.div`
  background-color: ${({ theme }) => theme.colors.lightGrey};
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: flex-start;
  width: 100%;
`;
