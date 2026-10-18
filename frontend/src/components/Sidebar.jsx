import React from 'react';
import { Box, VStack, Heading, Button, Icon, useColorModeValue } from '@chakra-ui/react';
import { FiMessageSquare, FiDatabase, FiSettings, FiHome } from 'react-icons/fi';
import { useNavigate, useLocation } from 'react-router-dom';

const Sidebar = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const bgColor = useColorModeValue('white', 'gray.800');
  const activeBg = useColorModeValue('primary.50', 'gray.700');
  const activeColor = useColorModeValue('primary.600', 'white');
  const inactiveColor = useColorModeValue('gray.600', 'gray.300');

  const menuItems = [
    { name: 'Accueil', path: '/chat', icon: FiHome },
    { name: 'Chat', path: '/chat', icon: FiMessageSquare },
    { name: 'Base de Connaissances', path: '/knowledge', icon: FiDatabase },
    { name: 'Paramètres', path: '/settings', icon: FiSettings },
  ];

  const isActive = (path) => location.pathname === path;

  return (
    <Box
      w="250px"
      h="100vh"
      bg={bgColor}
      borderRight="1px"
      borderRightColor="gray.200"
      p={4}
      position="fixed"
      left={0}
      top={0}
    >
      <VStack align="stretch" spacing={6} h="full">
        {/* Logo */}
        <Box>
          <Heading as="h1" size="lg" color="primary.500">
            Agentic AI
          </Heading>
          <Box fontSize="sm" color="gray.500" mt={1}>
            Assistant Intelligent
          </Box>
        </Box>

        {/* Menu */}
        <VStack align="stretch" spacing={2} flex={1}>
          {menuItems.map((item) => (
            <Button
              key={item.path}
              leftIcon={<Icon as={item.icon} />}
              justifyContent="flex-start"
              variant="ghost"
              bg={isActive(item.path) ? activeBg : 'transparent'}
              color={isActive(item.path) ? activeColor : inactiveColor}
              _hover={{ bg: activeBg, color: activeColor }}
              onClick={() => navigate(item.path)}
              fontWeight={isActive(item.path) ? 'bold' : 'normal'}
            >
              {item.name}
            </Button>
          ))}
        </VStack>

        {/* Footer */}
        <Box fontSize="xs" color="gray.500" textAlign="center">
          v1.0.0
        </Box>
      </VStack>
    </Box>
  );
};

export default Sidebar;
