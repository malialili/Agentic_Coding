import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import { Box, Flex } from '@chakra-ui/react';
import Sidebar from './components/Sidebar';
import ChatPage from './pages/ChatPage';
import KnowledgeBasePage from './pages/KnowledgeBasePage';
import SettingsPage from './pages/SettingsPage';

function App() {
  return (
    <Flex h="100vh" bg="gray.50">
      {/* Sidebar */}
      <Sidebar />
      
      {/* Contenu principal */}
      <Box flex={1} p={4} overflowY="auto">
        <Routes>
          <Route path="/" element={<Navigate to="/chat" replace />} />
          <Route path="/chat" element={<ChatPage />} />
          <Route path="/knowledge" element={<KnowledgeBasePage />} />
          <Route path="/settings" element={<SettingsPage />} />
        </Routes>
      </Box>
    </Flex>
  );
}

export default App;
